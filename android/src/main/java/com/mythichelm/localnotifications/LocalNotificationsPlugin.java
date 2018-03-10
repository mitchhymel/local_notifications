package com.mythichelm.localnotifications;

import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;

import java.util.List;
import java.util.ArrayList;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.PluginRegistry.NewIntentListener;
import io.flutter.view.FlutterNativeView;

/**
 * LocalNotificationsPlugin
 */
public class LocalNotificationsPlugin implements MethodCallHandler, NewIntentListener {
  public static final String LOGGING_TAG = "LocalNotificationsPlugin";
  public static final String CHANNEL_NAME = "plugins/local_notifications";
  public static final String CREATE_NOTIFICATION = "local_notifications_createNotification";
  public static final String REMOVE_NOTIFICATION = "local_notifications_removeNotification";
  public static final String CALLBACK_KEY = "callback_key";
  public static final String PAYLOAD_KEY = "payload_key";

  private final Registrar registrar;

  /**
   * Plugin registration.
   */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), LocalNotificationsPlugin.CHANNEL_NAME);
    LocalNotificationsPlugin plugin = new LocalNotificationsPlugin(registrar);
    channel.setMethodCallHandler(plugin);
    registrar.addNewIntentListener(plugin);

    LocalNotificationsService.setSharedChannel(channel);
  }

  private LocalNotificationsPlugin(Registrar registrar) {
    this.registrar = registrar;
  }

  private Context getActiveContext() {
    return (registrar.activity() != null) ? registrar.activity() : registrar.context();
  }

  @Override
  public boolean onNewIntent(Intent intent) {
    return handleIntent(intent);
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    List<Object> arguments = call.arguments();
    if (call.method.equals(CREATE_NOTIFICATION)) {
      result.success(createNotification(arguments));
    } else if (call.method.equals(REMOVE_NOTIFICATION)){
      int id = (int)arguments.get(0);
      removeNotification(id);
      result.success(null);
    } else {
      result.notImplemented();
    }
  }

  private int createNotification(List<Object> arguments) {
    String title = (String)arguments.get(0);
    String content = (String)arguments.get(1);
    String imageUrl = (String)arguments.get(2);
    String ticker = (String)arguments.get(3);
    int importance = (int)arguments.get(4);
    boolean isOngoing = (boolean)arguments.get(5);
    int id = (int)arguments.get(6);

    // get onNotificationclick
    String callbackName = (String)arguments.get(7);
    String actionText = (String)arguments.get(8);
    String intentPayload = (String)arguments.get(9);
    boolean launchesApp = (boolean)arguments.get(10);
    NotificationAction onNotificationClick = new NotificationAction(callbackName, actionText, intentPayload, launchesApp);

    // get extra actions
    List<String> actionsCallbacks = (List<String>)arguments.get(11);
    List<String> actionsTexts = (List<String>)arguments.get(12);
    List<String> actionsIntentPayloads = (List<String>)arguments.get(13);
    List<Boolean> actionsLaunchesApp = (List<Boolean>)arguments.get(14);
    List<NotificationAction> extraActions = new ArrayList<NotificationAction>();
    for (int i = 0; i < actionsCallbacks.size(); i++) {
      String callback = actionsCallbacks.get(i);
      String text = actionsTexts.get(i);
      String payload = actionsIntentPayloads.get(i);
      boolean launch = actionsLaunchesApp.get(i);
      extraActions.add(new NotificationAction(callback, text, payload, launch));
    }

    new GenerateLocalNotificationsTask(getActiveContext(),
            id, title, content, imageUrl, ticker, importance, isOngoing,
            onNotificationClick, extraActions)
            .execute();
    return id;
  }

  private void removeNotification(int id) {
    NotificationManager notificationManager =
            (NotificationManager) getActiveContext().getSystemService(Context.NOTIFICATION_SERVICE);
    notificationManager.cancel(id);
  }

  public static boolean handleIntent(Intent intent) {
    if (intent != null) {
      String callbackName = intent.getStringExtra(CALLBACK_KEY);
      String payload = intent.getStringExtra(PAYLOAD_KEY);
      if (callbackName != null && callbackName != "") {
        MethodChannel channel  = LocalNotificationsService.getSharedChannel();
        if (channel != null) {
          Log.d(LOGGING_TAG, "Invoking method " + callbackName + "(" + payload + ")");
          channel.invokeMethod(callbackName, payload);
          return true;
        }
        else {
          Log.d(LOGGING_TAG, "MethodChannel was null");
        }
      }
      else {
        Log.d(LOGGING_TAG, "callback name was null or empty");
      }
    }
    else {
      Log.d(LOGGING_TAG, "intent was null");
    }

    return false;
  }
}
