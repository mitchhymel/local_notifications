import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:local_notifications/local_notifications.dart';

void main() => runApp(new MyApp());

class MyApp extends StatelessWidget {
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
              new RaisedButton(
                onPressed: () async {
                  int id = await LocalNotifications.createNotification(
                      'Basic', 'some basic notification',
                  );
                },
                child: new Text('Create basic notification'),
              ),
              new RaisedButton(
                onPressed: () async {
                  int id = await LocalNotifications.createNotification(
                      'Image', 'some notification with an image',
                      imageUrl: 'https://flutter.io/images/catalog-widget-placeholder.png',
                  );
                },
                child: new Text('Create notification with image')
              ),
              new RaisedButton(
                  onPressed: () async {
                    int id = await LocalNotifications.createNotification(
                        'Title', 'content',
                        imageUrl: 'https://flutter.io/images/catalog-widget-placeholder.png',
                        isOngoing: true
                    );
                  },
                  child: new Text('Create undismissable notification')
              ),
              new RaisedButton(
                onPressed: () async {
                  await LocalNotifications.removeNotification(1);
                  debugPrint('removed notification');
                },
                child: new Text('Remove notification'),
              )
            ],
          ),
        ),
      ),
    );
  }
}

