part of local_notifications;

/// Class representing the iOS specific notification settings
///
/// The value of [presentWhileAppOpen] determines if the
/// notification will show to the user if the app is in the forefront
class IOSSettings {
  final bool presentWhileAppOpen;
  final int date;

  IOSSettings({
    this.presentWhileAppOpen = true,
    this.date
  });

  const IOSSettings._private({this.presentWhileAppOpen, this.date});

  Map _toMapForPlatformChannel() {
    return {
      'presentWhileAppOpen': presentWhileAppOpen
    };
  }
}