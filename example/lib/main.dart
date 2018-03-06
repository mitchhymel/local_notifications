import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:local_notifications/local_notifications.dart';

void main() => runApp(new MyApp());

class MyApp extends StatefulWidget {
  @override
  MyAppState createState() => new MyAppState();
}

class MyAppState extends State<MyApp> {

  String _imageUrl = 'https://flutter.io/images/catalog-widget-placeholder.png';
  String _text;

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

  Widget _getBasicNotification() {
    return new RaisedButton(
      onPressed: () async {
        int id = await LocalNotifications.createNotification(
          'Basic', 'some basic notification',
        );
      },
      child: new Text('Create basic notification'),
    );
  }

  Widget _getNotificationWithImage() {
    return new RaisedButton(
        onPressed: () async {
          int id = await LocalNotifications.createNotification(
            'Image', 'some notification with an image',
            imageUrl: _imageUrl,
          );
        },
        child: new Text('Create notification with image')
    );
  }

  Widget _getUndismissableNotification() {
    return new RaisedButton(
        onPressed: () async {
          int id = await LocalNotifications.createNotification(
              'No swiping', 'Can\'t swipe this away',
              imageUrl: _imageUrl,
              isOngoing: true
          );
        },
        child: new Text('Create undismissable notification')
    );
  }

  Widget _getRemoveNotification() {
    return new RaisedButton(
      onPressed: () async {
        // remove notificatino by id,
        // all examples don't provide an id, so it defaults to 0
        await LocalNotifications.removeNotification(0);
      },
      child: new Text('Remove notification'),
    );
  }

  Widget _getNotificationWithCallbackAndPayload() {
    return new RaisedButton(
      onPressed: () async {
        int id = await LocalNotifications.createNotification(
            'Callback and payload notif',
            'Some content',
            onNotificationClick: new NotificationAction(
                "some action", // Note: action text gets ignored here, as android can't display this anywhere
                onNotificationClick,
                "some payload"
            ),
        );
      },
      child: new Text('Create notification with payload and callback'),
    );
  }

  Widget _getNotificationWithCallbackAndPayloadInBackground() {
    return new RaisedButton(
      onPressed: () async {
        int id = await LocalNotifications.createNotification(
          'Callback and payload notif',
          'Some content',
          onNotificationClick: new NotificationAction(
              "some action", // Note: action text gets ignored here, as android can't display this anywhere
              onNotificationClick,
              "some payload without launching the app",
            launchesApp: false
          ),
        );
      },
      child: new Text('Create notification that executes callback without launching app'),
    );
  }

  Widget _getNotificationWithMultipleActionsAndPayloads() {
    return new RaisedButton(
      onPressed: () async {
        int id = await LocalNotifications.createNotification(
            'Multiple actions',
            '... and unique callbacks and/or payloads for each',
            imageUrl: _imageUrl,
            onNotificationClick: new NotificationAction(
                "some action",
                onNotificationClick,
                "some payload",
              launchesApp: false
            ),
            actions: [
              new NotificationAction(
                  "First",
                  onReplyClick,
                  "firstAction",
                launchesApp: true
              ),
              new NotificationAction(
                  "Second",
                  onReplyClick,
                  "secondAction",
                launchesApp: false
              ),
              new NotificationAction(
                  "Third",
                  onReplyClick,
                  "thirdAction",
                launchesApp: false
              ),
            ]
        );
      },
      child: new Text('Create notification with multiple actions'),
    );
  }

  @override
  Widget build(BuildContext context) {
    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('Notification example'),
        ),
        body: new Center(
          child: new Column(
            children: <Widget>[
              _getBasicNotification(),
              _getNotificationWithImage(),
              _getUndismissableNotification(),
              _getRemoveNotification(),
              _getNotificationWithCallbackAndPayload(),
              _getNotificationWithCallbackAndPayloadInBackground(),
              _getNotificationWithMultipleActionsAndPayloads(),
              new Text(_text ?? "Click a notification with a payload to see the results here")
            ],
          ),
        ),
      ),
    );
  }
}




