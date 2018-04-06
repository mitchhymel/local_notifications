package com.mythichelm.localnotifications;

import android.app.NotificationChannel;
import android.os.Build;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;

import com.mythichelm.localnotifications.entities.NotificationSettings;
import com.mythichelm.localnotifications.factories.INotificationSettingsFactory;
import com.mythichelm.localnotifications.factories.NotificationFactory;
import com.mythichelm.localnotifications.factories.NotificationSettingsFactory;
import com.mythichelm.localnotifications.services.LocalNotificationsService;


import java.util.ArrayList;
import java.util.HashMap;
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
    public static final String CREATE_NOTIFICATION_CHANNEL =
            "local_notifications_createNotificationChannel";
    public static final String REMOVE_NOTIFICATION_CHANNEL  =
            "local_notifications_removeNotificationChannel";
    public static final String SET_LOGGING = "local_notifications_setLogging";

    public static final String CALLBACK_KEY = "callback_key";
    public static final String PAYLOAD_KEY = "payload_key";

    public static boolean loggingEnabled = false;

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

    public static void customLog(String text) {
        if (loggingEnabled) {
            Log.d(LOGGING_TAG, "(Android): " + text);
        }
    }

    @Override
    public boolean onNewIntent(Intent intent) {
        return handleIntent(intent);
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        List<Object> arguments = call.arguments();
        LocalNotificationsPlugin.customLog("In onMethodCall for method '" + call.method + "'");
        switch (call.method) {
            case CREATE_NOTIFICATION:
                createNotification(arguments);
                result.success(null);
                break;
            case REMOVE_NOTIFICATION:
                int id = (int) arguments.get(0);
                removeNotification(id);
                result.success(null);
                break;
            case CREATE_NOTIFICATION_CHANNEL:
                createNotificationChannel(arguments);
                result.success(null);
                break;
            case REMOVE_NOTIFICATION_CHANNEL:
                removeNotificationChannel(arguments);
                result.success(null);
                break;
            case SET_LOGGING:
                boolean value = (boolean)arguments.get(0);
                loggingEnabled=value;
                result.success(null);
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    private void createNotification(List<Object> arguments) {
        NotificationSettings notificationSettings = notificationSettingsFactory.createFromArguments(arguments);
        new GenerateLocalNotificationsTask(
                getActiveContext(),
                notificationSettings,
                new NotificationFactory()
        ).execute();
    }

    private void removeNotification(int id) {
        NotificationManager notificationManager = getNotificationManager();
        notificationManager.cancel(id);
    }

    private void createNotificationChannel(List<Object> arguments) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getNotificationManager();
            if (notificationManager != null) {
                HashMap<String, Object> map = (HashMap<String, Object>)arguments.get(0);
                String channelId = (String) map.get("id");
                String name = (String) map.get("name");
                String description = (String) map.get("description");
                int importance = (int) map.get("importance");
                long[] vibratePattern = LocalNotificationsPlugin.intArrayListToLongArray(
                        (ArrayList<Integer>)map.get("vibratePattern"));

                NotificationChannel channel = new NotificationChannel(channelId, name, importance);
                channel.setDescription(description);
                channel.setVibrationPattern(vibratePattern);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void removeNotificationChannel(List<Object> arguments) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getNotificationManager();
            if (notificationManager != null) {
                String channelId = (String)arguments.get(0);
                notificationManager.deleteNotificationChannel(channelId);
            }
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getActiveContext().getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static boolean handleIntent(Intent intent) {
        if (intent == null) {
            customLog("intent was null");
            return false;
        }

        return checkAndInvokeCallback(intent);
    }

    private static boolean checkAndInvokeCallback(Intent intent) {
        String callbackName = intent.getStringExtra(CALLBACK_KEY);
        String payload = intent.getStringExtra(PAYLOAD_KEY);

        if (isNullOrEmpty(callbackName)) {
            customLog("callback name was null or empty");
            return false;
        }

        return invokeCallback(callbackName, payload);
    }

    private static boolean invokeCallback(String callbackName, String payload) {
        MethodChannel channel = LocalNotificationsService.getSharedChannel();
        if (channel != null) {
            customLog(String.format("Invoking method %1$s('%2$s')", callbackName, payload));
            channel.invokeMethod(callbackName, payload);
            return true;
        } else {
            customLog("MethodChannel was null");
            return false;
        }
    }

    private static boolean isNullOrEmpty(String callbackName) {
        return callbackName == null || Objects.equals(callbackName, "");
    }

    public static long[] intArrayListToLongArray(ArrayList<Integer> list) {
        long[] result = new long[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }
        return result;
    }

}
