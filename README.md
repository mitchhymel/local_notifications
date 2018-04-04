# LocalNotification Flutter Plugin

## Introduction

LocalNotifications is an easy way to create notifications on both [Android](https://developer.android.com/guide/topics/ui/notifiers/notifications.html) and [iOS](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/).

[![pub package](https://img.shields.io/pub/v/local_notifications.svg)](https://pub.dartlang.org/packages/local_notifications)

## Platforms

Currently the following platforms are supported
+ Android 6.0 (SDK version 23 or higher)
+ iOS 10 or higher

A goal is to bring this dependencies down as much as possible. 


## Features

+ Create Notification with custom **Title** and **Content**
+ Create Notification with **Custom Callback**
+ Create Notification with multiple **Actions**
+ [Android Only] Create Notification with an **Image**
+ [Android Only] Create Notification which is **Undismissable**
+ [Android Only] Set **Priority** / **Importance** of Notification
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

```
await LocalNotifications.createNotification(
    title: "Basic",
    content: "Notification",
    id: 0
);
```

When you want to create multiple notifications make sure to assign each one an unique id

```
await LocalNotifications.createNotification(
    title: "My First Notification",
    content: "SomeContent"
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

```
int notificationId = 0;

await LocalNotifications.createNotification(
    title: "My First Notification",
    content: "SomeContent"
    id: notificationId
);

await LocalNotifications.removeNotification(notificationId);
```

## Notification with custom callback

You can create notifications, which execute a custom callback in the background instead of launching the app.

```
onNotificationClick(String payload) {
	// payload is "some payload"
	print('Running in background and received payload: $payload');
}

int id = await LocalNotifications.createNotification(
    title: "Notification",
    content: "With custom callback",
    id: 0,
    onNotificationClick: new NotificationAction(
        actionText: "some action", // Note: only works for iOS
        callback: onNotificationClick,
        payload: "some payload"
    )
);
```

## Notifications with multiple actions

You can create notifications which have multiple action buttons

```
handleCustomActionClick(String payload) {
    if(payload == "secondAction") {
        LocalNotifications.removeNotification(0);
    }
}

int id = await LocalNotifications.createNotification(
    title: "Multiple Actions",
    content: 'With custom callbacks',
    id: 0,
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
