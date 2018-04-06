part of local_notifications;

/// Class representing the iOS specific notification settings
class IOSSettings {
  final bool presentWhileAppOpen;
  IOSSettings({
    this.presentWhileAppOpen = true
  });

  const IOSSettings._private({this.presentWhileAppOpen});

  static const IOSSettings DEFAULT = const IOSSettings._private(
    presentWhileAppOpen: true
  );

  Map _toMapForPlatformChannel() {
    return {
      'presentWhileAppOpen': presentWhileAppOpen
    };
  }
}