import 'dart:async';

import 'package:flutter/services.dart';


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
        int importance = ANDROID_IMPORTANCE_DEFAULT , bool isOngoing = false,
        int id = 1
      }) {
    List args = [
      title,
      content,
      imageUrl,
      ticker,
      importance,
      isOngoing,
      id
    ];
    return _channel.invokeMethod('createNotification', args);
  }

  static Future<Null> removeNotification(int id) {
    return _channel.invokeMethod('removeNotification', [id]);
  }
}

