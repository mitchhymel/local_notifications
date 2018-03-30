package com.mythichelm.localnotifications;

import android.app.Notification;
import android.content.Context;

public interface INotifcationFactory {
    Notification createNotification(NotificationSettings settings, Context context);
}
