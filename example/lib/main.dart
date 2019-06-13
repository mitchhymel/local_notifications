import 'package:flutter/material.dart';
import 'package:local_notifications/local_notifications.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  MyAppState createState() => MyAppState();
}

class MyAppState extends State<MyApp> {
  String _imageUrl = 'https://flutter.io/images/catalog-widget-placeholder.png';
  String _text;
  bool loggingEnabled = false;

  onNotificationClick(String payload) {
    setState(() {
      _text = 'in onNotificationClick with payload: $payload';
    });
  }

  onReplyClick(String payload) {
    setState(() {
      _text = 'in onReplyClick with payload: $payload';
    });
    LocalNotifications.removeNotification(0);
  }

  void removeNotify(String payload) async {
    await LocalNotifications.removeNotification(0);
  }

  static const AndroidNotificationChannel channel =
      const AndroidNotificationChannel(
    id: 'default_notification11',
    name: 'CustomNotificationChannel',
    description: 'Grant this app the ability to show notifications',
    importance: AndroidNotificationChannelImportance.HIGH,
    vibratePattern: AndroidVibratePatterns.DEFAULT,
  );

  Widget _getAddNotificationChannelButton() {
    return RaisedButton(
      child: const Text('Create a notification channel (Android 8.0+)'),
      onPressed: () async {
        await LocalNotifications.createAndroidNotificationChannel(
            channel: channel);
      },
    );
  }

  Widget _getRemoveNotificationChannelButton() {
    return RaisedButton(
      child: const Text('Remove a notification channel (Android 8.0+)'),
      onPressed: () async {
        await LocalNotifications.removeAndroidNotificationChannel(
            channel: channel);
      },
    );
  }

  Widget _enableLogging() {
    return Row(
      children: <Widget>[
        Switch(
            value: loggingEnabled,
            onChanged: (val) async {
              setState(() {
                loggingEnabled = val;
              });
              await LocalNotifications.setLogging(val);
            }),
        const Text('Enable or Disable logging')
      ],
    );
  }

  Widget _getBasicNotification() {
    return RaisedButton(
      onPressed: () async {
        await LocalNotifications.createNotification(
            id: 0,
            title: 'Basic',
            content: 'some basic notification',
            androidSettings: AndroidSettings(
              isOngoing: false,
              channel: channel,
              priority: AndroidNotificationPriority.HIGH,
            ),
            onNotificationClick: NotificationAction(
                actionText: "some action",
                callback: removeNotify,
                payload: ""));
      },
      child: const Text('Create basic notification'),
    );
  }

  Widget _getNotificationWithImage() {
    return RaisedButton(
        onPressed: () async {
          await LocalNotifications.createNotification(
            id: 0,
            title: 'Image',
            content: 'some notification with an image',
            imageUrl: _imageUrl,
          );
        },
        child: const Text('Create notification with image'));
  }

  Widget _getUndismissableNotification() {
    return RaisedButton(
        onPressed: () async {
          await LocalNotifications.createNotification(
              id: 0,
              title: 'No swiping',
              content: 'Can\'t swipe this away',
              imageUrl: _imageUrl,
              androidSettings: AndroidSettings(isOngoing: true));
        },
        child: const Text('Create undismissable notification'));
  }

  Widget _getRemoveNotification() {
    return RaisedButton(
      onPressed: () async {
        // remove notificatino by id,
        // all examples don't provide an id, so it defaults to 0
        await LocalNotifications.removeNotification(0);
      },
      child: const Text('Remove notification'),
    );
  }

  Widget _getNotificationWithCallbackAndPayload() {
    return RaisedButton(
      onPressed: () async {
        await LocalNotifications.createNotification(
          id: 0,
          title: 'Callback and payload notif',
          content: 'Some content',
          onNotificationClick: NotificationAction(
              actionText:
                  "some action", // Note: action text gets ignored here, as android can't display this anywhere
              callback: onNotificationClick,
              payload: "some payload"),
        );
      },
      child: const Text('Create notification with payload and callback'),
    );
  }

  Widget _getNotificationWithCallbackAndPayloadInBackground() {
    return RaisedButton(
      onPressed: () async {
        await LocalNotifications.createNotification(
          id: 0,
          title: 'Callback and payload notif',
          content: 'Some content',
          onNotificationClick: NotificationAction(
              actionText:
                  "some action", // Note: action text gets ignored here, as android can't display this anywhere
              callback: onNotificationClick,
              payload: "some payload without launching the app",
              launchesApp: false),
        );
      },
      child: const Text(
          'Create notification that executes callback without launching app'),
    );
  }

  Widget _getNotificationWithMultipleActionsAndPayloads() {
    return RaisedButton(
      onPressed: () async {
        await LocalNotifications.createNotification(
            id: 0,
            title: 'Multiple actions',
            content: '... and unique callbacks and/or payloads for each',
            imageUrl: _imageUrl,
            onNotificationClick: NotificationAction(
                actionText: "some action",
                callback: onNotificationClick,
                payload: "some payload",
                launchesApp: false),
            actions: [
              NotificationAction(
                  actionText: "First",
                  callback: onReplyClick,
                  payload: "firstAction",
                  launchesApp: true),
              NotificationAction(
                  actionText: "Second",
                  callback: onReplyClick,
                  payload: "secondAction",
                  launchesApp: false),
              NotificationAction(
                  actionText: "Third",
                  callback: onReplyClick,
                  payload: "thirdAction",
                  launchesApp: false),
            ]);
      },
      child: const Text('Create notification with multiple actions'),
    );
  }

  Widget _getNotificationWithAnonymousFunctionAsCallback() {
    return RaisedButton(
        child: const Text(
            'Create notification with anonymous function as callback using a callbackName'),
        onPressed: () async {
          await LocalNotifications.createNotification(
              id: 0,
              title: 'Anonymous callback',
              content:
                  '... using anonymous callback with provided callbackName',
              onNotificationClick: NotificationAction(
                  actionText: '', //ignored
                  callback: (String payload) {
                    setState(() {
                      _text = payload;
                    });
                  },
                  payload: 'payload with anonymous function',
                  callbackName: 'anonymousName'),
              actions: [
                NotificationAction(
                  actionText: 'anon',
                  callback: (String payload) {
                    setState(() {
                      _text = payload;
                    });
                  },
                  payload: 'payload from action with anonymous action',
                  callbackName: 'anonymousAction',
                )
              ]);
        });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Notification example'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              _enableLogging(),
              _getBasicNotification(),
              _getNotificationWithImage(),
              _getUndismissableNotification(),
              _getRemoveNotification(),
              _getNotificationWithCallbackAndPayload(),
              _getNotificationWithCallbackAndPayloadInBackground(),
              _getNotificationWithMultipleActionsAndPayloads(),
              _getNotificationWithAnonymousFunctionAsCallback(),
              _getAddNotificationChannelButton(),
              _getRemoveNotificationChannelButton(),
              Text(_text ??
                  "Click a notification with a payload to see the results here")
            ],
          ),
        ),
      ),
    );
  }
}
