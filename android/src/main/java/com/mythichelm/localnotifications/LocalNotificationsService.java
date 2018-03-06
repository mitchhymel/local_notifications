package com.mythichelm.localnotifications;

import android.os.Bundle;
import android.content.Intent;
import android.app.IntentService;
import android.util.Log;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import io.flutter.view.FlutterNativeView;

public class LocalNotificationsService extends IntentService {
    private static MethodChannel sSharedChannel;

    public LocalNotificationsService() {
        super("LocalNotificationsService");
    }

    public static MethodChannel getSharedChannel() {
        return sSharedChannel;
    }

    public static boolean setSharedChannel(MethodChannel channel) {
        if (sSharedChannel != null && sSharedChannel != channel) {
            Log.d(LocalNotificationsPlugin.LOGGING_TAG, "sSharedChannel tried to overwrite an existing Registrar");
            return false;
        }
        Log.d(LocalNotificationsPlugin.LOGGING_TAG, "sSharedChannel set");
        sSharedChannel = channel;
        return true;
    }

    @Override
    public void onHandleIntent(Intent intent) {
        LocalNotificationsPlugin.handleIntent(intent);
    }
}