# local_notifications

A [Flutter](https://flutter.io/) plugin for showing and removing notifications. Currently only supports Android.

## Usage

See the full example [here](https://github.com/mitchhymel/local_notifications/blob/master/example/lib/main.dart)

## Android set up steps
If you want to just display a notification, and have your app open after the notification is clicked, then this can be done fully with Dart code and there's not changese needed to the native Android part of the app. 

To have the notification make a callback to dart code, you have to override your launcher activity's (usually MainActivity) onNewIntent

In "\android\app\src\main\java\com\...\MainActivity.java"

Add the following imports
```
import com.mythichelm.localnotifications.LocalNotificationsPlugin;
import android.content.Intent;
```

Then add the following to your onNewIntent
```
@Override
protected void onNewIntent(Intent intent) {
  LocalNotificationsPlugin.handleIntent(intent); // Add me for callbacks
}
```


On the dart side of things, create a MethodChannel in order to get messages from Android and write your callback function
```
class MyAppState extends State<MyApp> {
  static const String notificationCallbackName = "onNotificationClick"; // make sure this is the same name as your function
  static const channel = const MethodChannel(LocalNotifications.CHANNEL_NAME);
  
  @override
  void initState() {
    super.initState();

    channel.setMethodCallHandler((MethodCall method) {
      var payload = method.arguments;
      if (method.method == notificationCallbackName) {
        onNotificationClick(payload);
      }
      else {
        print("no method found: ${method.method}, $payload");
      }
    });
  }
    
  onNotificationClick(String payload) {
    print('in onNotificationClick with payload: $payload');
  }
    
  createNotification() {
    int id = await LocalNotifications.createNotification(
              'Callback and payload notif',
              'Some content',
              onNotificationClick: new NotificationAction(
                  "some action", // Note: action text gets ignored here, as android can't display this anywhere
                  notificationCallbackName,
                  "some payload"
              ),
          );
  }
}
```



To have the notification run code in the background (on click of either the notification itself or its actions), you must add the LocalNotificationsService to your app's manifest. Add the following to the <Application> tag of your manifest (android\app\src\main\AndroidManifest.xml)
```
<service
  android:name="com.mythichelm.localnotifications.LocalNotificationsService"
  android:exported="false" />
```


## Examples

### Creating a basic notification with title and text content
```
int id = await LocalNotifications.createNotification(
                  'Basic', 'some basic notification',
              );
```


### Creating a notification with an image
```
int id = await LocalNotifications.createNotification(
                  'Image', 'some notification with an image',
                  imageUrl: 'https://flutter.io/images/catalog-widget-placeholder.png',
              );
```

### Creating an undismissable notification
```
int id = await LocalNotifications.createNotification(
                  'Title', 'content',
                  imageUrl: 'https://flutter.io/images/catalog-widget-placeholder.png',
                  isOngoing: true
              );
```

### Removing a notification
```
await LocalNotifications.removeNotification(1);
```


## Getting Started

For help getting started with Flutter, view our online
[documentation](http://flutter.io/).

For help on editing plugin code, view the [documentation](https://flutter.io/platform-plugins/#edit-code).
