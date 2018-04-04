package com.mythichelm.localnotifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.mythichelm.localnotifications.entities.NotificationSettings;
import com.mythichelm.localnotifications.factories.INotifcationFactory;

import java.io.*;
import java.lang.ref.WeakReference;
import java.net.*;

public class GenerateLocalNotificationsTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<Context> mContext;
    private final NotificationSettings notificationSettings;
    private INotifcationFactory notifcationFactory;

    GenerateLocalNotificationsTask(Context context, NotificationSettings notificationSettings, INotifcationFactory notifcationFactory) {
        super();
        this.mContext = new WeakReference<>(context);
        this.notificationSettings = notificationSettings;
        this.notifcationFactory = notifcationFactory;
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

        if (notificationManager != null)
            notificationManager.notify(notificationSettings.Id, notification);
    }

    private Notification createNotification() {
        return this.notifcationFactory
                .createNotification(notificationSettings, this.mContext.get());
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) this.mContext.get()
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }
}