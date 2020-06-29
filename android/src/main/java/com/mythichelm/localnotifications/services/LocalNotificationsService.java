package com.mythichelm.localnotifications.services;

import android.content.Intent;
import android.app.IntentService;
import android.util.Log;

import com.mythichelm.localnotifications.LocalNotificationsPlugin;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class LocalNotificationsService extends IntentService {
    private static MethodChannel sSharedChannel;
    private static Registrar intentRegistrar;

    public LocalNotificationsService() {
        super("LocalNotificationsService");
    }

    public static MethodChannel getSharedChannel() {
        return sSharedChannel;
    }

    public static void setRegistrat(Registrar registrar) {
        intentRegistrar = registrar;
    }

    public static void setSharedChannel(MethodChannel channel) {
        if (sSharedChannel != null && sSharedChannel != channel) {
            Log.d(LocalNotificationsPlugin.LOGGING_TAG, "sSharedChannel tried to overwrite an existing Registrar");
            return;
        }
        Log.d(LocalNotificationsPlugin.LOGGING_TAG, "sSharedChannel set");
        sSharedChannel = channel;
    }

    @Override
    public void onHandleIntent(Intent intent) {
        LocalNotificationsPlugin.customLog("LocalNotificationsService handling intent in the background");
        LocalNotificationsPlugin.handleIntent(intent, intentRegistrar);
    }
}