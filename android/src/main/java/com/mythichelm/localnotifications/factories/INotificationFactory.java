package com.mythichelm.localnotifications.factories;

import android.app.Notification;
import android.content.Context;

import com.mythichelm.localnotifications.entities.NotificationSettings;

public interface INotificationFactory {
    Notification createNotification(NotificationSettings settings, Context context);
}
