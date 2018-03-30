package com.mythichelm.localnotifications.factories;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.provider.Settings;

import com.mythichelm.localnotifications.entities.NotificationAction;
import com.mythichelm.localnotifications.entities.NotificationSettings;

public class NotificationFactory implements INotifcationFactory {

    @Override
    public Notification createNotification(NotificationSettings settings, Context context) {
        Icon applicationIcon = getApplicationIcon(context);

        @SuppressLint("WrongConstant") Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(settings.Title)
                .setContentText(settings.Body)
                .setSmallIcon(applicationIcon)
                .setOngoing(settings.IsOngoing)
                .setContentIntent(settings.ContentIntent)
                .setPriority(settings.Priority)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        setLargeIcon(builder, settings);
        setTicker(builder, settings);
        addActions(builder, settings, context, applicationIcon);

        return builder.build();
    }


    private void addActions(Notification.Builder builder, NotificationSettings settings, Context context, Icon icon) {
        for (NotificationAction extraAction : settings.ExtraActions) {
            PendingIntent intent = extraAction.getIntent(context);
            Notification.Action action = new Notification.Action.Builder(icon, extraAction.actionText, intent).build();
            builder.addAction(action);
        }
    }

    private void setTicker(Notification.Builder builder, NotificationSettings settings) {
        if (settings.Ticker != null)
            builder.setTicker(settings.Ticker);
    }

    private void setLargeIcon(Notification.Builder builder, NotificationSettings settings) {
        if (settings.LargeIcon != null)
            builder.setLargeIcon(settings.LargeIcon);
    }

    private Icon getApplicationIcon(Context context) {
        BitmapDrawable drawable = null;

        try {
            drawable = (BitmapDrawable) context
                    .getPackageManager()
                    .getApplicationIcon(context.getPackageName());
        } catch (PackageManager.NameNotFoundException ignored) {}

        Bitmap bitmap = Bitmap.createBitmap(16, 16, Bitmap.Config.RGB_565);
        if(drawable != null)
            bitmap = drawable.getBitmap();

        return Icon.createWithBitmap(bitmap);
    }

}