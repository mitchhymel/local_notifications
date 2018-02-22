import 'dart:async';

import 'package:flutter/services.dart';

class NotificationAction {
  final String callbackFunctionName;
  final String actionText;
  final String intentPayload;
  const NotificationAction(this.actionText, this.callbackFunctionName, this.intentPayload);

  static const NotificationAction DEFAULT = const NotificationAction('', '', '');
}

class LocalNotifications {
  static const MethodChannel _channel =
      const MethodChannel('local_notifications');

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
      callbacks,
      actionTexts,
      intentPayloads
    ];
    return _channel.invokeMethod('createNotification', args);
  }

  static Future<Null> removeNotification(int id) {
    return _channel.invokeMethod('removeNotification', [id]);
  }
}

