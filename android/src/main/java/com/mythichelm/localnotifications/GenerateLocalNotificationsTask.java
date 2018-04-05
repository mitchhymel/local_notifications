package com.mythichelm.localnotifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;

import com.mythichelm.localnotifications.entities.NotificationSettings;
import com.mythichelm.localnotifications.factories.INotificationFactory;

import java.io.*;
import java.lang.ref.WeakReference;
import java.net.*;

public class GenerateLocalNotificationsTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<Context> mContext;
    private final NotificationSettings notificationSettings;
    private INotificationFactory notificationFactory;

    GenerateLocalNotificationsTask(Context context, NotificationSettings notificationSettings, INotificationFactory notificationFactory) {
        super();
        this.mContext = new WeakReference<>(context);
        this.notificationSettings = notificationSettings;
        this.notificationFactory = notificationFactory;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (notificationSettings.ImageUrl == null || notificationSettings.ImageUrl.equals("")) {
            return null;
        }

        InputStream in;
        try {
            URL url = new URL(notificationSettings.ImageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            return BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        notificationSettings.LargeIcon = result;
        Notification notification = createNotification();
        NotificationManager notificationManager = getNotificationManager();

        if (notificationManager != null) {
            LocalNotificationsPlugin.customLog("notificationManager.notify");
            notificationManager.notify(notificationSettings.Id, notification);
        }

    }

    private Notification createNotification() {
        return this.notificationFactory
                .createNotification(notificationSettings, this.mContext.get());
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) this.mContext.get()
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }
}