part of local_notifications;

class LocalNotifications {
  static const MethodChannel _channel =
  const MethodChannel(CHANNEL_NAME);

  static const String CHANNEL_NAME = 'plugins/local_notifications';
  static const String _createNotification =
      'local_notifications_createNotification';
  static const String _removeNotification =
      'local_notifications_removeNotification';
  static const String _createNotificationChannel =
      'local_notifications_createNotificationChannel';
  static const String _removeNotificationChannel =
      'local_notifications_removeNotificationChannel';
  static const String _setLogging = 'local_notifications_setLogging';

  static bool loggingEnabled = false;

  static void _log(String text) {
    if (loggingEnabled) {
      print('LocalNotificationsPlugin (Dart): $text');
    }
  }

  /// Enable or disable logging for debugging issues
  static Future<Null> setLogging(bool value) async {
    loggingEnabled = value;
    await _channel.invokeMethod(_setLogging, [value]);
  }

  /// Creates a local notification.
  ///
  /// Creates a local notification with a title using [title] and content
  /// using [content].
  ///
  /// Only on Android, if [imageUrl] is provided, then the
  /// notification will contain this image.
  ///
  /// Only on Android, the value provided by [importance] will be used
  /// by the OS to determine how to rank your notification against other
  /// apps' notifications
  ///
  /// Only on Android, the value of [isOngoing] determines if the
  /// notification can be dismissed or not.
  ///
  /// The notification will be created with the value provided by [id]. If no
  /// value is provided, the notification id will default to 0
  ///
  /// Only on iOS, the value of [presentWhileAppOpen] determines if the
  /// notification will show to the user if the app is in the forefront
  ///
  /// The value of [onNotificationClick] determines what happens when the
  /// notification is clicked. If no value is provided, the default action is
  /// to simply launch the app.
  ///
  /// The value of [actions] determines the actions of the notification.
  /// Both android and ios support a limited number of actions.
  static Future<Null> createNotification ({
    @required String title,
    @required String content,
    @required int id,
    String imageUrl = '',
    String ticker = '',
    NotificationAction onNotificationClick = NotificationAction.DEFAULT,
    List<NotificationAction> actions = const [],
    AndroidSettings androidSettings = AndroidSettings.DEFAULT,
    IOSSettings iOSSettings = IOSSettings.DEFAULT,
  }) async {

    _channel.setMethodCallHandler((MethodCall method) {
      var payload = method.arguments;
      List<NotificationAction> actionsToCheck = []..add(onNotificationClick)..addAll(actions);
      for (NotificationAction action in actionsToCheck) {
        String functionName = NotificationAction._getCallbackNameFromAction(action);
        if (method.method == functionName) {
          _log('After action is clicked. Calling $functionName("$payload")');
          action.callback(payload);
          return;
        }
      }

      _log('After action is clicked. No method found matching name "${method.method}"');
    });

    List extraActionsAsMaps = actions.map((a) => a._toMapForPlatformChannel()).toList();

    Map argsMap = {
      'title': title,
      'content': content,
      'imageUrl': imageUrl,
      'ticker': ticker,
      'id': id,
      'onNotificationClick': onNotificationClick._toMapForPlatformChannel(),
      'extraActions': extraActionsAsMaps,
      'androidSettings': androidSettings._toMapForPlatformChannel(),
      'iOSSettings': iOSSettings._toMapForPlatformChannel(),
    };

    _log('Invoking "$_createNotification" with map="$argsMap"');
    await _channel.invokeMethod(_createNotification, [argsMap]);
  }

  /// Removes a local notification with the provided [id].
  static Future<Null> removeNotification(int id) async {
    await _channel.invokeMethod(_removeNotification, [id]);
  }

  /// (Android only) Creates a notification channel
  ///
  /// This is necessary for your app to be able to send notifications on
  /// Android 8.0+
  static Future<Null> createAndroidNotificationChannel({
    @required AndroidNotificationChannel channel
  }) async {
    if (defaultTargetPlatform == TargetPlatform.android) {
      await _channel.invokeMethod(_createNotificationChannel, [channel._toMapForPlatformChannel()]);
    }
  }

  /// (Android only) Removes a notification channel
  ///
  /// This only works on Android 8.0+. Otherwise it is a no-op.
  static Future<Null> removeAndroidNotificationChannel({
    @required AndroidNotificationChannel channel
  }) async {
    if (defaultTargetPlatform == TargetPlatform.android) {
      await _channel.invokeMethod(_removeNotificationChannel, [channel.id]);
    }
  }
}