part of local_notifications;

/// Class representing the Android specific notification settings
///
/// The value provided by [importance] will be used
/// by the OS to determine how to rank your notification against other
/// apps' notifications
///
/// The value of [isOngoing] determines if the
/// notification can be dismissed or not.
///
/// (Only for Android 8.0+) The value of [channel] decides which channel
/// the notification is posted to.
///
/// The value of [vibratePattern] determines how many times and how long
/// the phone will vibrate when the notification is posted. From the android
/// SDK documentation:
/// 'Pass in an array of ints that are the durations for which to turn on or
/// off the vibrator in milliseconds. The first value indicates the number of
/// milliseconds to wait before turning the vibrator on. The next value
/// indicates the number of milliseconds for which to keep the vibrator on
/// before turning it off. Subsequent values alternate between durations in
/// milliseconds to turn the vibrator off or to turn the vibrator on.'
/// https://developer.android.com/reference/android/os/Vibrator.html#vibrate(long[],%20int)
class AndroidSettings {
  final AndroidNotificationImportance importance;
  final AndroidNotificationChannel channel;
  final bool isOngoing;
  final List<int> vibratePattern;

  AndroidSettings({
    this.importance=AndroidNotificationImportance.HIGH,
    this.channel,
    this.isOngoing=false,
    this.vibratePattern=const []
  });

  const AndroidSettings._private({
    this.importance,
    this.channel,
    this.isOngoing,
    this.vibratePattern
  });

  static const AndroidSettings DEFAULT = const AndroidSettings._private(
    importance: AndroidNotificationImportance.HIGH,
    channel: null,
    isOngoing: false,
    vibratePattern: const []
  );

  Map _toMapForPlatformChannel() {
    return {
      'isOngoing': isOngoing,
      'channel': channel == null ? '': channel.id,
      'importance': importance.val,
      'vibratePattern': vibratePattern
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
  final List<int> vibratePattern;

  const AndroidNotificationChannel({
    @required this.id,
    @required this.name,
    @required this.description,
    @required this.importance,
    this.vibratePattern = const[],
  });

  Map _toMapForPlatformChannel() {
    return {
      'id': this.id,
      'name': this.name,
      'description': this.description,
      'importance': this.importance.val,
      'vibratePattern': this.vibratePattern,
    };
  }
}