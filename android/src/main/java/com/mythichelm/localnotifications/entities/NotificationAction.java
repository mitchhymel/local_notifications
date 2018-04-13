package com.mythichelm.localnotifications.entities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.mythichelm.localnotifications.LocalNotificationsPlugin;
import com.mythichelm.localnotifications.services.LocalNotificationsService;

import java.util.Objects;

public class NotificationAction {
    private String callbackFunctionName;
    public String actionText;
    private String payload;
    private boolean launchesApp;
    private static int currentId = 0;

    public NotificationAction(String callbackFunctionName, String actionText, String payload, boolean launchesApp) {
        this.callbackFunctionName = callbackFunctionName;
        this.actionText = actionText;
        this.payload = payload;
        this.launchesApp = launchesApp;
    }

    private boolean isEmptyAction() {
        return "".equals(this.callbackFunctionName)
                && "".equals(this.actionText)
                && "".equals(this.payload);
    }

    public PendingIntent getIntent(Context context) {
        return launchesApp
                ? getIntentForLaunchesApp(context)
                : getIntentNotLaunchesApp(context);
    }

    private PendingIntent getIntentNotLaunchesApp(Context context) {
        Intent actionIntent = new Intent(context, LocalNotificationsService.class);
        addActionsToIntent(actionIntent);
        return PendingIntent.getService(context, currentId++, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getIntentForLaunchesApp(Context context) {
        Intent actionIntent = context
                .getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());

        addActionsToIntent(actionIntent);
        return PendingIntent.getActivity(context, currentId++, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void addActionsToIntent(Intent actionIntent) {
        if (!isEmptyAction() && actionIntent != null) {
            actionIntent.putExtra(LocalNotificationsPlugin.CALLBACK_KEY, callbackFunctionName);
            actionIntent.putExtra(LocalNotificationsPlugin.PAYLOAD_KEY, payload);
        }
    }
}