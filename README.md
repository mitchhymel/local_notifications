# local_notifications

A [Flutter](https://flutter.io/) plugin for showing and removing notifications. Currently only supports Android.

## Usage

See the full example [here](https://github.com/mitchhymel/local_notifications/blob/master/example/lib/main.dart)

## Android set up steps
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

### Creating a notification that will run code in background on click
```
onNotificationClick(String payload) {
	// payload is 'some payload'
	print('Running in background and received payload: $payload');
}


int id = await LocalNotifications.createNotification(
            'Some title',
            'Some content',
            onNotificationClick: new NotificationAction(
                "this_is_ignored", // title of action is ignored for notification click
                onNotificationClick, // your callback
                "some payload",
              launchesApp: false
            ),
        );
```


## Getting Started

For help getting started with Flutter, view our online
[documentation](http://flutter.io/).

For help on editing plugin code, view the [documentation](https://flutter.io/platform-plugins/#edit-code).
