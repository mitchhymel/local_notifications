import 'package:flutter/material.dart';
import 'package:local_notifications/local_notifications.dart';
import 'dart:async';

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

  var staticId = 1;
  void removeNotify(String payload) {
    LocalNotifications.removeNotification(staticId);
  }

  Widget _getBasicNotification() {
    return new RaisedButton(
      onPressed: () async {
        staticId = await LocalNotifications.createNotification(
          title: 'Basic',
          content: 'some basic notification',
          isOngoing: false,
          onNotificationClick: new NotificationAction(
              actionText: "some action",
              callback: removeNotify,
              payload: "")
        );
      },
      child: new Text('Create basic notification'),
    );
  }

  Widget _getNotificationWithImage() {
    return new RaisedButton(
        onPressed: () async {
          int id = await LocalNotifications.createNotification(
            title: 'Image',
            content: 'some notification with an image',
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
              title: 'No swiping',
              content: 'Can\'t swipe this away',
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
            title: 'Callback and payload notif',
            content: 'Some content',
            onNotificationClick: new NotificationAction(
                actionText: "some action", // Note: action text gets ignored here, as android can't display this anywhere
                callback: onNotificationClick,
                payload: "some payload"
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
          title: 'Callback and payload notif',
          content: 'Some content',
          onNotificationClick: new NotificationAction(
              actionText:  "some action", // Note: action text gets ignored here, as android can't display this anywhere
              callback: onNotificationClick,
              payload: "some payload without launching the app",
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
            title: 'Multiple actions',
            content: '... and unique callbacks and/or payloads for each',
            imageUrl: _imageUrl,
            onNotificationClick: new NotificationAction(
                actionText: "some action",
                callback: onNotificationClick,
                payload: "some payload",
                launchesApp: false
            ),
            actions: [
              new NotificationAction(
                  actionText: "First",
                  callback: onReplyClick,
                  payload: "firstAction",
                  launchesApp: true
              ),
              new NotificationAction(
                  actionText: "Second",
                  callback: onReplyClick,
                  payload: "secondAction",
                  launchesApp: false
              ),
              new NotificationAction(
                  actionText: "Third",
                  callback: onReplyClick,
                  payload: "thirdAction",
                  launchesApp: false
              ),
            ]
        );
      },
      child: new Text('Create notification with multiple actions'),
    );
  }

  Widget _getNotificationWithAnonymousFunctionAsCallback() {
    return new RaisedButton(
        child: new Text('Create notification with anonymous function as callback using a callbackName'),
        onPressed: () async {
          int id = await LocalNotifications.createNotification(
            title: 'Anonymous callback',
            content: '... using anonymous callback with provided callbackName',
            onNotificationClick: new NotificationAction(
              actionText: '', //ignored
              callback: (String payload) {
                setState((){
                  _text = payload;
                });
              },
              payload: 'payload with anonymous function',
              callbackName: 'anonymousName'
            )
          );
        }
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
              _getNotificationWithAnonymousFunctionAsCallback(),
              new Text(_text ?? "Click a notification with a payload to see the results here")
            ],
          ),
        ),
      ),
    );
  }
}




