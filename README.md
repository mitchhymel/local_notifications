# local_notifications

A [Flutter](https://flutter.io/) plugin for local notifications for [Android](https://developer.android.com/guide/topics/ui/notifiers/notifications.html) and [iOS](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/). Available on [Pub](https://pub.dartlang.org/packages/local_notifications).

## Usage

See the full example [here](https://github.com/mitchhymel/local_notifications/blob/master/example/lib/main.dart)

## Features supported by platform
| Feature        | Android           | iOS 10+  |
| ------------- |:-----:|:-----:|
| Create local notification with custom title and content    | <ul><li> - [x] </li></ul>| <ul><li> - [x] </li></ul> |
| Create local notification with custom actions with custom callbacks  | <ul><li> - [x] </li></ul>| <ul><li> - [x] </li></ul> |
| Dismiss local notification which is already delivered | <ul><li> - [x] </li></ul>| <ul><li> - [x] </li></ul> |
| Create local notification with an image  | <ul><li> - [x] </li></ul>| <ul><li> - [ ] </li></ul> |
| Create local notification which is undismissable | <ul><li> - [x] </li></ul>| <ul><li> - [ ] </li></ul> |

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
          title: 'Basic',
          content: 'some basic notification',
        );
```

<img src="https://github.com/mitchhymel/local_notifications/blob/master/gifs/basic_notification.gif" height="800" width="440">

### Creating a notification with an image
```
int id = await LocalNotifications.createNotification(
            title: 'Image',
            content: 'some notification with an image',
            imageUrl: _imageUrl,
          );
```
<img src="https://github.com/mitchhymel/local_notifications/blob/master/gifs/notification_with_image.gif" height="800" width="440">

### Creating an undismissable notification
```
int id = await LocalNotifications.createNotification(
              title: 'No swiping',
              content: 'Can\'t swipe this away',
              imageUrl: _imageUrl,
              isOngoing: true
          );
```
<img src="https://github.com/mitchhymel/local_notifications/blob/master/gifs/undismissable.gif" height="800" width="440">

### Removing a notification
```
await LocalNotifications.removeNotification(id);
```
<img src="https://github.com/mitchhymel/local_notifications/blob/master/gifs/remove_notification.gif" height="800" width="440">

### Creating a notification that will run code in background on click
```
onNotificationClick(String payload) {
	// payload is 'some payload'
	print('Running in background and received payload: $payload');
}


int id = await LocalNotifications.createNotification(
            title: 'Callback and payload notif',
            content: 'Some content',
            onNotificationClick: new NotificationAction(
                actionText: "some action", // Note: action text gets ignored here, as android can't display this anywhere
                callback: onNotificationClick,
                payload: "some payload"
            ),
        );
```
<img src="https://github.com/mitchhymel/local_notifications/blob/master/gifs/notification_with_payload_in_background.gif" height="800" width="440">

### Creating a notification with multiple actions, some run in background, some bring app to foreground
For code, see the [example](https://github.com/mitchhymel/local_notifications/blob/master/example/lib/main.dart#L112).


<img src="https://github.com/mitchhymel/local_notifications/blob/master/gifs/notification_multiple_actions.gif" height="800" width="440">

## Getting Started

For help getting started with Flutter, view our online
[documentation](http://flutter.io/).

For help on editing plugin code, view the [documentation](https://flutter.io/platform-plugins/#edit-code).
