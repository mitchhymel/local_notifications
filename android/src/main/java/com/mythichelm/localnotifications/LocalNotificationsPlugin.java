package com.mythichelm.localnotifications;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import android.content.Context;
import android.app.NotificationManager;

import java.util.List;
import java.util.ArrayList;

/**
 * LocalNotificationsPlugin
 */
public class LocalNotificationsPlugin implements MethodCallHandler {
  private final Registrar mRegistrar;
  private final MethodChannel mChannel;

  /**
   * Plugin registration.
   */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "local_notifications");
    channel.setMethodCallHandler(new LocalNotificationsPlugin(registrar, channel));
  }

  private LocalNotificationsPlugin(Registrar registrar, MethodChannel channel) {
    this.mRegistrar = registrar;
    this.mChannel = channel;
  }

  private Context getActiveContext() {
    return (mRegistrar.activity() != null) ? mRegistrar.activity() : mRegistrar.context();
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    List<Object> arguments = call.arguments();
    if (call.method.equals("createNotification")) {
      result.success(createNotification(arguments));
    } else if (call.method.equals("removeNotification")){
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
    NotificationAction onNotificationClick = new NotificationAction(callbackName, actionText, intentPayload);

    // get extra actions
    List<String> actionsCallbacks = (List<String>)arguments.get(10);
    List<String> actionsTexts = (List<String>)arguments.get(11);
    List<String> actionsIntentPayloads = (List<String>)arguments.get(12);
    List<NotificationAction> extraActions = new ArrayList<NotificationAction>();
    for (int i = 0; i < actionsCallbacks.size(); i++) {
      String callback = actionsCallbacks.get(i);
      String text = actionsTexts.get(i);
      String payload = actionsIntentPayloads.get(i);
      extraActions.add(new NotificationAction(callback, text, payload));
    }

    new GenerateLocalNotificationTask(getActiveContext(),
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
}
