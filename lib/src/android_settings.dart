part of local_notifications;

/// Class representing the Android specific notification settings
///
/// The value provided by [priority] will be used
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
/// the phone will vibrate when the notification is posted. Unless you have a
/// need for custom vibration pattern, you can ignore this or use
/// [AndroidVibratePatterns.DEFAULT] to fallback to the phone's default pattern.
/// If you explicitly want no vibration, use [AndroidVibratePatterns.NONE].
///
/// From the android SDK documentation:
/// 'Pass in an array of ints that are the durations for which to turn on or
/// off the vibrator in milliseconds. The first value indicates the number of
/// milliseconds to wait before turning the vibrator on. The next value
/// indicates the number of milliseconds for which to keep the vibrator on
/// before turning it off. Subsequent values alternate between durations in
/// milliseconds to turn the vibrator off or to turn the vibrator on.'
/// https://developer.android.com/reference/android/os/Vibrator.html#vibrate(long[],%20int)
class AndroidSettings {
  final AndroidNotificationPriority priority;
  final AndroidNotificationChannel channel;
  final bool isOngoing;
  final List<int> vibratePattern;

  AndroidSettings({
    this.priority=AndroidNotificationPriority.HIGH,
    this.channel,
    this.isOngoing=false,
    this.vibratePattern=AndroidVibratePatterns.DEFAULT,
  });

  const AndroidSettings._private({
    this.priority,
    this.channel,
    this.isOngoing,
    this.vibratePattern
  });

  static const AndroidSettings DEFAULT = const AndroidSettings._private(
    priority: AndroidNotificationPriority.HIGH,
    channel: null,
    isOngoing: false,
    vibratePattern: AndroidVibratePatterns.DEFAULT
  );

  Map _toMapForPlatformChannel() {
    return {
      'isOngoing': isOngoing,
      'channel': channel == null ? '': channel.id,
      'priority': priority.val,
      'vibratePattern': vibratePattern,
      'useDefaultVibratePattern': vibratePattern == AndroidVibratePatterns.DEFAULT,
    };
  }
}

/// A helper class to provide values for [AndroidSettings.vibratePattern]
///
/// Using the value of [DEFAULT] for [AndroidSettings.vibratePattern] means
/// that when the notification is posted, the phone will use it's default
/// vibrate pattern.
///
/// Using the value of [NONE] for [AndroidSettings.vibratePattern] means that
/// when the notification is posted, the phone will not vibrate. To be exact,
/// the phone will wait to vibrate for 0 milliseconds and then not vibrate at all.
/// In order for a notification to show up as a heads up notification on Android
/// versions before 26, the notification vibrate pattern must be set, even
/// if that pattern means that the phone doesn't actually vibrate.
class AndroidVibratePatterns {
  static const List<int> DEFAULT = const [1];
  static const List<int> NONE = const [0];
  const AndroidVibratePatterns._private();
}

/// Enum representing the Android IMPORTANCE enum
///
/// Reference:
/// https://developer.android.com/reference/android/app/NotificationManager.html#IMPORTANCE_DEFAULT
class AndroidNotificationChannelImportance {
  final int val;
  const AndroidNotificationChannelImportance._private(this.val);

  static const AndroidNotificationChannelImportance MIN = const AndroidNotificationChannelImportance._private(1);
  static const AndroidNotificationChannelImportance LOW = const AndroidNotificationChannelImportance._private(2);
  static const AndroidNotificationChannelImportance DEFAULT = const AndroidNotificationChannelImportance._private(3);
  static const AndroidNotificationChannelImportance HIGH = const AndroidNotificationChannelImportance._private(4);
  static const AndroidNotificationChannelImportance MAX = const AndroidNotificationChannelImportance._private(5);
}

/// Enum representing the Android PRIORITY enum
///
/// Reference:
/// https://developer.android.com/reference/android/app/Notification.html#PRIORITY_DEFAULT
class AndroidNotificationPriority {
  final int val;
  const AndroidNotificationPriority._private(this.val);

  static const AndroidNotificationPriority MIN = const AndroidNotificationPriority._private(-2);
  static const AndroidNotificationPriority LOW = const AndroidNotificationPriority._private(-1);
  static const AndroidNotificationPriority DEFAULT = const AndroidNotificationPriority._private(0);
  static const AndroidNotificationPriority HIGH = const AndroidNotificationPriority._private(1);
  static const AndroidNotificationPriority MAX = const AndroidNotificationPriority._private(2);
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
/// The value of [importance] determines the default value for the priority
/// of notifications on this channel.
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
  final AndroidNotificationChannelImportance importance;
  final List<int> vibratePattern;

  const AndroidNotificationChannel({
    @required this.id,
    @required this.name,
    @required this.description,
    @required this.importance,
    this.vibratePattern = AndroidVibratePatterns.DEFAULT,
  });

  Map _toMapForPlatformChannel() {
    return {
      'id': this.id,
      'name': this.name,
      'description': this.description,
      'importance': this.importance.val,
      'vibratePattern': this.vibratePattern,
      'useDefaultVibratePattern': vibratePattern == AndroidVibratePatterns.DEFAULT,
    };
  }
}