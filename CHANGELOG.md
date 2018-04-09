# [0.0.3] - April 8, 2018

* Added setLogging method to enable/disable extra logging for help with debugging
* Added support for Android 8.0+ with ability to create notification channels

# [0.0.2] - April 3, 2018

* createNotification parameter "id" is now required and no longer returns anything
* removeNotification no longer returns anything
* On Android, the default notification importance is high (was ANDROID_IMPORTANCE_DEFAULT)
* Fixed exception thrown in createNotification when using Dart 2
* Android min SDK supported is now 23 (this was always the case, it is now just formerly specified in the project's gradle)

## [0.0.1] - March 24, 2018

* Initial release of plugin
* Below is a table indicating the features supported in this release


| Feature        | Android           | ios  |
| ------------- |:-----:|:-----:|
| Create local notification with custom title and content    | <ul><li> - [x] </li></ul>| <ul><li> - [x] </li></ul> |
| Create local notification with custom actions with custom callbacks  | <ul><li> - [x] </li></ul>| <ul><li> - [x] </li></ul> |
| Dismiss local notification which is already delivered | <ul><li> - [x] </li></ul>| <ul><li> - [x] </li></ul> |
| Create local notification with an image  | <ul><li> - [x] </li></ul>| <ul><li> - [ ] </li></ul> |
| Create local notification which is undismissable | <ul><li> - [x] </li></ul>| <ul><li> - [ ] </li></ul> |
