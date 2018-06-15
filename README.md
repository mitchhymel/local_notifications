# LocalNotification Flutter Plugin

## Plugin is no longer actively developed

I currently don't have plans to continue active development of this plugin. If something is not working or a feature is missing, feel free to submit a pull request. Otherwise, check out [flutter_local_notifications](https://github.com/MaikuB/flutter_local_notifications).


## Introduction

LocalNotifications is an easy way to create notifications on both [Android](https://developer.android.com/guide/topics/ui/notifiers/notifications.html) and [iOS](https://developer.apple.com/library/content/documentation/NetworkingInternet/Conceptual/RemoteNotificationsPG/).

[![pub package](https://img.shields.io/pub/v/local_notifications.svg)](https://pub.dartlang.org/packages/local_notifications)

## Platforms

Currently the following platforms are supported
+ All Android versions that Flutter supports (4.1+ or SDK 16+)
+ iOS 10 or higher

A goal is to bring this dependencies down as much as possible. 


## Features

+ Create Notification with custom **Title** and **Content**
+ Create Notification with **Custom Callback**
+ Create Notification with multiple **Actions**
+ [Android Only] Create Notification with an **Image**
+ [Android Only] Create Notification which is **Undismissable**
+ [Android Only] Set **Priority** / **Importance** of Notification
+ [Android Only] Create a Notification with a custom vibrate pattern
+ **Remove** Notification by id

## Installation

Simply add this package to your `pubspec.yaml`
```
dependencies:
  local_notifications: any
```

Then run `flutter packages get` to install the package.

For iOS, There is an issue with the flutter framework (https://github.com/flutter/flutter/issues/16097) that will cause build errors in projects initialized with objective-c iOS code when using plugins written in swift (as is the case with this plugin). To get around this for an existing project, see [this comment](https://github.com/mitchhymel/local_notifications/issues/5#issuecomment-377344269). For new projects, just create the project using swift with ```flutter create -i swift```

### Add Service and Permission to AndroidManifest

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

To support Android 4.1 and 4.2, you also need to add the following to inside the `manifest` node.
There should already be an existing permission for INTERNET, just add this below that. For a full explanation why this is needed, see this [issue](https://github.com/mitchhymel/local_notifications/issues/10#issuecomment-380997029)
```
<uses-permission android:name="android.permission.VIBRATE" />
```

## Send your first notification

Sending a basic notification is simple. 
(If running on an Android 8.0+ device, see bottom section "Sending Notifications on Android 8.0+")

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


## Sending Notifications on Android 8.0+

For Android 8.0+, you must first create a Notification Channel and associate this channel with every notification you create,
otherwise the notifications will not be shown. Read about them in the [Android SDK docs](https://developer.android.com/guide/topics/ui/notifiers/notifications.html#ManageChannels).

```
// Initialize your Notification channel object
static const AndroidNotificationChannel channel = const AndroidNotificationChannel(
      id: 'default_notification',
      name: 'Default',
      description: 'Grant this app the ability to show notifications',
      importance: AndroidNotificationImportance.HIGH
);

// Create the notification channel (this is a no-op on iOS and android <8.0 devices)
// Only need to run this one time per App install, any calls after that will be a no-op at the native level
// but will still need to use the platform channel. For this reason, avoid calling this except for the 
// first time you need to create the channel.
await LocalNotifications.createAndroidNotificationChannel(channel: channel);

// Create your notification, providing the channel info
await LocalNotifications.createNotification(
    title: "Basic",
    content: "Notification",
    id: 0,
    androidSettings: new AndroidSettings(
        channel: channel
    )
);
```

## Send a notification that shows a heads up notification on Android and iOS
The below code is an example to have your notifications be shown as a heads up notification
on iOS and all Android versions. Some of the values used in the constructors are the same as
the constructors default values, but I'm being explicit here to show exactly what values
you need.

```
// Initialize your Notification channel object
// For a heads up notification, the importance must be HIGH
static const AndroidNotificationChannel channel = const AndroidNotificationChannel(
      id: 'some_channel_id',
      name: 'My app feature that requires notifications',
      description: 'Grant this app the ability to show notifications for this app feature',
      importance: AndroidNotificationImportance.HIGH, // default value for constructor
      vibratePattern: AndroidVibratePatterns.DEFAULT, // default value for constructor
);

// Create the notification channel (this is a no-op on iOS and android <8.0 devices)
// Only need to run this one time per channel to initialize, any calls after that will be a no-op at the native level
// but will still need to use the platform channel. For this reason, avoid calling this except for the 
// first time you need to create the channel.
await LocalNotifications.createAndroidNotificationChannel(channel: channel);

// Create your notification, providing the channel info
await LocalNotifications.createNotification(
    title: "Basic",
    content: "Notification",
    id: 0,
    androidSettings: new AndroidSettings(
        channel: channel,
        priority: AndroidNotificationPriority.HIGH, // default value for constructor
        vibratePattern: AndroidVibratePatterns.DEFAULT, // default value for constructor
    ),
    iOSSettings: new iOSSettings (
        presentWhileAppOpen: true, // default value for constructor
    ),
);
```
