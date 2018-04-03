# LocalNotification Flutter Plugin

```
Changelog:
    2018-03-30: Initial Creation
```

## Introduction

LocalNotifications is an easy way to create notifications on both [Android](https://developer.android.com/guide/topics/ui/notifiers/notifications.html) and [iOS](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/).
This packages is also available on [Pub](https://pub.dartlang.org/packages/local_notifications).


## Features

+ Create Notification with custom **Title** and **Content**
+ Create Notification with **Custom Callback**
+ Create Notification with multiple **Actions**
+ [Android Only] Create Notification with an **Image**
+ [Android Only] Create Notification which is **Undismissable**
+ **Remove** Notification by id

## Installation

Simply add this package to your `pubspec.yaml`
```
dependencies:
  local_notifications: any
```

Then run `flutter packages get` to install the package.

### Add Service to AndroidManifest

To have the notification run code in the background (on click of either the notification itself or its actions), you must add the LocalNotificationsService to your app's manifest.

First locate your `AndroidManifest.xml` file, which is located at

```
android/app/src/main/AndroidManifest.xml
```

Then add the following Tag to the `application` node

```
<service
    android:name="com.mythichelm.localnotifications.services.LocalNotificationsService"
    android:exported="false" />
```

An example how it should look like can be found [HERE](https://github.com/mitchhymel/local_notifications/blob/master/example/android/app/src/main/AndroidManifest.xml)


## Send your first notification

Sending a basic notification is simple

```dart
await LocalNotifications.createNotification(
    title: "Basic",
    content: "Notification"
);
```

When you want to create multiple notifications make sure to assign each one an unique id

```dart
await LocalNotifications.createNotification(
    title: "My First Notification",
    content: "SomeContent",
    id: 0
);

await LocalNotifications.createNotification(
    title: "My Second Notification",
    content: "SomeContent"
    id: 1
);
```

## Delete Notifications

You can delete the notifications by calling `removeNotification` with the notification id

```dart
int notificationId = 0;

await LocalNotifications.createNotification(
    title: "My First Notification",
    content: "SomeContent",
    id: notificationId
);

await LocalNotifications.removeNotification(notificationId);
```

## Notification with custom callback

You can create notifications, which execute a custom callback in the background instead of launching the app.

```dart
onNotificationClick(String payload) {
    // payload is "some payload"
    print('Running in background and received payload: $payload');
}

await LocalNotifications.createNotification(
    title: "Notification",
    content: "With custom callback",
    onNotificationClick: new NotificationAction(
        actionText: "some action", // Note: only works for iOS
        callback: onNotificationClick,
        payload: "some payload"
    )
);
```

## Notifications with multiple actions

You can create notifications which have multiple action buttons

```dart
handleCustomActionClick(String payload) {
    if(payload == "secondAction") {
        LocalNotifications.removeNotification(0);
    }
}

await LocalNotifications.createNotification(
    title: "Multiple Actions",
    content: 'With custom callbacks',
    onNotificationClick: new NotificationAction(
        actionText: "Some action",
        callback: onNotificationClick,
        payload: "Some payload",
        launchesApp: false
    ),
    actions: [
        new NotificationAction(
            actionText: "First",
            callback: handleCustomActionClick,
            payload: "firstAction",
            launchesApp: true
        ),
        new NotificationAction(
            actionText: "Second",
            callback: handleCustomActionClick,
            payload: "secondAction",
            launchesApp: false
        )
    ]
);
```

## [Android Only] Create Notification with Large Image

You can create notifications with an large image in addition to the app icon

```dart
await LocalNotifications.createNotification(
    title: "Notification",
    content: "With Image",
    imageUrl: "https://flutter.io/images/catalog-widget-placeholder.png"
);
```


## [Android Only] Create Ongoing / Undismissable Notifications

You can create ongoing notifications. This means, the user is unable to swipe them away.

```dart
await LocalNotifications.createNotification(
    title: "Notification",
    content: "Can\'t swipe this away",
    isOngoing: true
);
```