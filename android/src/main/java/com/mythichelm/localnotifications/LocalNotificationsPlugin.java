package com.mythichelm.localnotifications;

import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;

import com.mythichelm.localnotifications.entities.NotificationSettings;
import com.mythichelm.localnotifications.factories.INotificationSettingsFactory;
import com.mythichelm.localnotifications.factories.NotificationFactory;
import com.mythichelm.localnotifications.factories.NotificationSettingsFactory;
import com.mythichelm.localnotifications.services.LocalNotificationsService;


import java.util.List;
import java.util.Objects;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.plugin.common.PluginRegistry.NewIntentListener;

/**
 * LocalNotificationsPlugin
 */
public class LocalNotificationsPlugin implements MethodCallHandler, NewIntentListener {
    public static final String LOGGING_TAG = "LocalNotifications";
    public static final String CHANNEL_NAME = "plugins/local_notifications";
    public static final String CREATE_NOTIFICATION = "local_notifications_createNotification";
    public static final String REMOVE_NOTIFICATION = "local_notifications_removeNotification";
    public static final String CALLBACK_KEY = "callback_key";
    public static final String PAYLOAD_KEY = "payload_key";

    private final Registrar registrar;
    private final INotificationSettingsFactory notificationSettingsFactory = new NotificationSettingsFactory();

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
        switch (call.method) {
            case CREATE_NOTIFICATION:
                result.success(createNotification(arguments));
                break;
            case REMOVE_NOTIFICATION:
                int id = (int) arguments.get(0);
                removeNotification(id);
                result.success(null);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private int createNotification(List<Object> arguments) {
        NotificationSettings notificationSettings = notificationSettingsFactory.createFromArguments(arguments);
        new GenerateLocalNotificationsTask(
                getActiveContext(),
                notificationSettings,
                new NotificationFactory()
        ).execute();
        return notificationSettings.Id;
    }

    private void removeNotification(int id) {
        NotificationManager notificationManager = getNotificationManager();
        notificationManager.cancel(id);
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getActiveContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static boolean handleIntent(Intent intent) {
        if (intent == null) {
            Log.d(LOGGING_TAG, "intent was null");
            return false;
        }

        return checkAndInvokeCallback(intent);
    }

    private static boolean checkAndInvokeCallback(Intent intent) {
        String callbackName = intent.getStringExtra(CALLBACK_KEY);
        String payload = intent.getStringExtra(PAYLOAD_KEY);

        if (isNullOrEmpty(callbackName)) {
            Log.d(LOGGING_TAG, "callback name was null or empty");
            return false;
        }

        return invokeCallback(callbackName, payload);
    }

    private static boolean invokeCallback(String callbackName, String payload) {
        MethodChannel channel = LocalNotificationsService.getSharedChannel();
        if (channel != null) {
            Log.d(LOGGING_TAG, "Invoking method " + callbackName + "('" + payload + "')");
            channel.invokeMethod(callbackName, payload);
            return true;
        } else {
            Log.d(LOGGING_TAG, "MethodChannel was null");
            return false;
        }
    }

    private static boolean isNullOrEmpty(String callbackName) {
        return callbackName == null || Objects.equals(callbackName, "");
    }
}
