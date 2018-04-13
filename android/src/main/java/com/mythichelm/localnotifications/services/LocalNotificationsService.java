package com.mythichelm.localnotifications.services;

import android.content.Intent;
import android.app.IntentService;
import android.util.Log;

import com.mythichelm.localnotifications.LocalNotificationsPlugin;

import io.flutter.plugin.common.MethodChannel;

public class LocalNotificationsService extends IntentService {
    private static MethodChannel sSharedChannel;

    public LocalNotificationsService() {
        super("LocalNotificationsService");
    }

    public static MethodChannel getSharedChannel() {
        return sSharedChannel;
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
        LocalNotificationsPlugin.handleIntent(intent);
    }
}