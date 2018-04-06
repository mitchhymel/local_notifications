part of local_notifications;

/// Class representing the Android specific notification settings
class AndroidSettings {
  final AndroidNotificationImportance importance;
  final AndroidNotificationChannel channel;
  final bool isOngoing;

  AndroidSettings({
    this.importance=AndroidNotificationImportance.HIGH,
    this.channel,
    this.isOngoing=false
  });

  const AndroidSettings._private({
    this.importance,
    this.channel,
    this.isOngoing=false
  });

  static const AndroidSettings DEFAULT = const AndroidSettings._private(
    importance: AndroidNotificationImportance.HIGH,
    channel: null,
    isOngoing: false
  );

  Map _toMapForPlatformChannel() {
    return {
      'isOngoing': isOngoing,
      'channel': channel == null ? '': channel.id,
      'importance': importance.val
    };
  }
}

/// Enum representing the Android IMPORTANCE enum
///
/// Reference:
/// https://developer.android.com/reference/android/app/NotificationManager.html#IMPORTANCE_DEFAULT
class AndroidNotificationImportance {
  final int val;
  const AndroidNotificationImportance._private(this.val);

  static const AndroidNotificationImportance DEFAULT = const AndroidNotificationImportance._private(3);
  static const AndroidNotificationImportance HIGH = const AndroidNotificationImportance._private(4);
  static const AndroidNotificationImportance LOW = const AndroidNotificationImportance._private(2);
  static const AndroidNotificationImportance MAX = const AndroidNotificationImportance._private(5);
  static const AndroidNotificationImportance MIN = const AndroidNotificationImportance._private(1);
}

/// Class that describes an Android Notification Channel (for android 8.0+)
///
/// The [name] is how the user identifies your notification channels, while [id]
/// is how your app should identify the channels and what you must use when
/// creating notifications. [id] is also used to with
/// [removeAndroidNotificationChannel].
///
/// The [description] is meant to provide a short description of this channel.
///
/// The value of [importance] determines the default value for notifications
/// on this channel.
///
/// Android 8.0 added Notification Channels, which allow users to opt in or
/// out of notifications more granularly than at the app level.
/// https://developer.android.com/guide/topics/ui/notifiers/notifications.html#ManageChannels
///
/// For managing notification channels, reference:
/// https://developer.android.com/training/notify-user/channels.html
class AndroidNotificationChannel {
  final String id;
  final String name;
  final String description;
  final AndroidNotificationImportance importance;

  const AndroidNotificationChannel({
    @required this.id,
    @required this.name,
    @required this.description,
    @required this.importance
  });

  Map _toMapForPlatformChannel() {
    return {
      'id': this.id,
      'name': this.name,
      'description': this.description,
      'importance': this.importance.val,
    };
  }
}