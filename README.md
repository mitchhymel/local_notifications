# local_notifications

A [Flutter](https://flutter.io/) plugin for showing and removing notifications. Currently only supports Android.

## Usage

See the full example [here](https://github.com/mitchhymel/local_notifications/blob/master/example/lib/main.dart)

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
