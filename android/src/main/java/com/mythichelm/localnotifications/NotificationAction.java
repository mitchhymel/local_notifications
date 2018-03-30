package com.mythichelm.localnotifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

class NotificationAction {
    private String callbackFunctionName;
    String actionText;
    private String intentPayload;
    private boolean launchesApp;
    private static int currentId = 0;

    NotificationAction(String callbackFunctionName, String actionText, String intentPayload, boolean launchesApp) {
        this.callbackFunctionName = callbackFunctionName;
        this.actionText = actionText;
        this.intentPayload = intentPayload;
        this.launchesApp = launchesApp;
    }

    private boolean isEmptyAction() {
        return Objects.equals(this.callbackFunctionName, "")
                && Objects.equals(this.actionText, "")
                && Objects.equals(this.intentPayload, "");
    }

    PendingIntent getIntent(Context context) {
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
            actionIntent.putExtra(LocalNotificationsPlugin.PAYLOAD_KEY, intentPayload);
        }
    }
}