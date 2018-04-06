part of local_notifications;

/// Class that describes a notification action.
///
/// When the user clicks a notification action, the function [callback] will
/// be called with [payload] as the argument.
///
/// The value of [launchesApp] determines if the app is brought into the
/// forefront when the action is clicked. The default value is true
///
/// The value of [callbackName] must be provided if [callback] is an anonymous
/// function. Else this value will be populated by this library from the
/// name of [callback].
class NotificationAction {
  final Function(String) callback;
  final String actionText;
  final String payload;
  final bool launchesApp;
  final String callbackName; // only use when callback is an anonymous function
  const NotificationAction({
    @required this.actionText,
    @required this.callback,
    @required this.payload,
    this.launchesApp = true,
    this.callbackName
  });

  static const NotificationAction DEFAULT = const NotificationAction(
      actionText: '',
      callback: null,
      payload: ''
  );

  Map _toMapForPlatformChannel() {
    return {
      'callback': _getCallbackNameFromAction(this),
      'actionText': actionText,
      'payload': payload,
      'launchesApp': launchesApp,
    };
  }

  static String _getCallbackNameFromAction(NotificationAction action) {
    return action.callbackName ?? _nameOfFunction(action.callback);
  }

  // Extracts the name of a top-level function from the .toString() of its
  // closure-ization. The Java side of this plugin accepts the entrypoint into
  // Dart code as a string. However, the Dart side of this API can't use a
  // string to specify the entrypoint, otherwise it won't be visited by Dart's
  // AOT compiler.
  static String _nameOfFunction(Function(String) callback) {
    if (callback == null) {
      return '';
    }

    final String longName = callback.toString();
    final int functionIndex = longName.indexOf('Function');
    if (functionIndex == -1) return null;
    final int openQuote = longName.indexOf("'", functionIndex + 1);
    if (openQuote == -1) return null;
    final int closeQuote = longName.indexOf("'", openQuote + 1);
    if (closeQuote == -1) return null;
    return longName.substring(openQuote + 1, closeQuote);
  }
}