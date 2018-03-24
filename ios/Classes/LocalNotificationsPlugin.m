#import "LocalNotificationsPlugin.h"
#import <local_notifications/local_notifications-Swift.h>

@implementation LocalNotificationsPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftLocalNotificationsPlugin registerWithRegistrar:registrar];
}
@end
