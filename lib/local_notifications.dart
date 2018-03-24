import 'dart:async';
import 'package:meta/meta.dart';

import 'package:flutter/services.dart';

class NotificationAction {
  final Function(String) callback;
  final String actionText;
  final String payload;
  final bool launchesApp;
  final String callbackName; // only use when callback is an anonymous function
  const NotificationAction({
      @required this.actionText,
      @required this.callback,
      @required this.payload,
      this.launchesApp = true,
      this.callbackName
  });

  static const NotificationAction DEFAULT = const NotificationAction(
      actionText: '',
      callback: null,
      payload: ''
  );
}

class LocalNotifications {
  static const MethodChannel _channel =
      const MethodChannel(CHANNEL_NAME);

  static const String CHANNEL_NAME = 'plugins/local_notifications';
  static const String _createNotification = 'local_notifications_createNotification';
  static const String _removeNotification = 'local_notifications_removeNotification';

  static const int ANDROID_IMPORTANCE_DEFAULT = 3;
  static const int ANDROID_IMPORTANCE_HIGH = 4;
  static const int ANDROID_IMPORTANCE_LOW = 2;
  static const int ANDROID_IMPORTANCE_MAX = 5;
  static const int ANDROID_IMPORTANCE_MIN = 1;

  static Future<int> createNotification({
    @required String title,
    @required String content,
    String imageUrl = '',
    String ticker = '',
    int importance = ANDROID_IMPORTANCE_DEFAULT,
    bool isOngoing = false,
    int id = 0,
    bool presentWhileAppOpen = true,
    NotificationAction onNotificationClick = NotificationAction.DEFAULT,
    List<NotificationAction> actions = const []
  }) {
    List<String> callbacks = actions.map((action) => _getCallbackNameFromAction(action)).toList();
    List<String> actionTexts = actions.map((action) => action.actionText).toList();
    List<String> intentPayloads = actions.map((action) => action.payload).toList();
    List<bool> launchesApps = actions.map((action) => action.launchesApp).toList();

    _channel.setMethodCallHandler((MethodCall method) {
      var payload = method.arguments;
      List<NotificationAction> actionsToCheck = []..add(onNotificationClick)..addAll(actions);
      for (NotificationAction action in actionsToCheck) {
        String functionName = _getCallbackNameFromAction(action);
        if (method.method == functionName) {
          print('Dart: calling $functionName with payload: $payload');
          action.callback(payload);
          return;
        }
      }

      print('Dart: no method found: ${method.method}, $payload');
    });

    List args = [
      title,
      content,
      imageUrl,
      ticker,
      importance,
      isOngoing,
      id,
      _getCallbackNameFromAction(onNotificationClick),
      onNotificationClick.actionText,
      onNotificationClick.payload,
      onNotificationClick.launchesApp,
      callbacks,
      actionTexts,
      intentPayloads,
      launchesApps,
      presentWhileAppOpen
    ];
    return _channel.invokeMethod(_createNotification, args);
  }

  static Future<Null> removeNotification(int id) {
    return _channel.invokeMethod(_removeNotification, [id]);
  }

  static String _getCallbackNameFromAction(NotificationAction action) {
    return action.callbackName ?? _nameOfFunction(action.callback);
  }

  // Extracts the name of a top-level function from the .toString() of its
  // closure-ization. The Java side of this plugin accepts the entrypoint into
  // Dart code as a string. However, the Dart side of this API can't use a
  // string to specify the entrypoint, otherwise it won't be visited by Dart's
  // AOT compiler.
  static String _nameOfFunction(Function(String) callback) {
    if (callback == null) {
      return '';
    }

    final String longName = callback.toString();
    final int functionIndex = longName.indexOf('Function');
    if (functionIndex == -1) return null;
    final int openQuote = longName.indexOf("'", functionIndex + 1);
    if (openQuote == -1) return null;
    final int closeQuote = longName.indexOf("'", openQuote + 1);
    if (closeQuote == -1) return null;
    return longName.substring(openQuote + 1, closeQuote);
  }

}

