part of local_notifications;

/// Class representing the iOS specific notification settings
///
/// The value of [presentWhileAppOpen] determines if the
/// notification will show to the user if the app is in the forefront
class IOSSettings {
  final bool presentWhileAppOpen;
  IOSSettings({this.presentWhileAppOpen = true});

  const IOSSettings._private({this.presentWhileAppOpen});

  static const IOSSettings DEFAULT =
      const IOSSettings._private(presentWhileAppOpen: true);

  Map _toMapForPlatformChannel() {
    return {'presentWhileAppOpen': presentWhileAppOpen};
  }
}
