import 'dart:async';

import 'package:flutter/services.dart';

class NotificationAction {
  final String callbackFunctionName;
  final String actionText;
  final String intentPayload;
  final bool launchesApp;
  const NotificationAction(this.actionText, this.callbackFunctionName, this.intentPayload, {this.launchesApp = true});

  static const NotificationAction DEFAULT = const NotificationAction('', '', '');
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

  static Future<Null> createNotification(String title, String content,
      {String imageUrl = '', String ticker = '',
        int importance = ANDROID_IMPORTANCE_DEFAULT,
        bool isOngoing = false, int id = 0,
        NotificationAction onNotificationClick = NotificationAction.DEFAULT,
        List<NotificationAction> actions = const []
      }) {
    List<String> callbacks = actions.map((action) => action.callbackFunctionName).toList();
    List<String> actionTexts = actions.map((action) => action.actionText).toList();
    List<String> intentPayloads = actions.map((action) => action.intentPayload).toList();
    List<bool> launchesApps = actions.map((action) => action.launchesApp).toList();

    List args = [
      title,
      content,
      imageUrl,
      ticker,
      importance,
      isOngoing,
      id,
      onNotificationClick.callbackFunctionName,
      onNotificationClick.actionText,
      onNotificationClick.intentPayload,
      onNotificationClick.launchesApp,
      callbacks,
      actionTexts,
      intentPayloads,
      launchesApps
    ];
    return _channel.invokeMethod(_createNotification, args);
  }

  static Future<Null> removeNotification(int id) {
    return _channel.invokeMethod(_removeNotification, [id]);
  }
}

