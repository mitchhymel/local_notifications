package com.mythichelm.localnotifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.*;
import java.lang.ref.WeakReference;
import java.net.*;

import java.util.List;

public class GenerateLocalNotificationsTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<Context> mContext;
    private int id;
    private String title, content, imageUrl, ticker;
    private int importance;
    private boolean isOngoing;
    private NotificationAction onNotificationClick;
    private List<NotificationAction> extraActions;
    private INotifcationFactory notifcationFactory;

    GenerateLocalNotificationsTask(Context context, int id, String title, String content,
                                   String imageUrl, String ticker, int importance, boolean isOngoing,
                                   NotificationAction onNotificationClick,
                                   List<NotificationAction> extraActions, INotifcationFactory notifcationFactory) {
        super();
        this.mContext = new WeakReference<>(context);
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.ticker = ticker;
        this.importance = importance;
        this.isOngoing = isOngoing;
        this.onNotificationClick = onNotificationClick;
        this.extraActions = extraActions;
        this.notifcationFactory = notifcationFactory;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        if (this.imageUrl == null || this.imageUrl.equals("")) {
            return null;
        }

        InputStream in;
        try {
            URL url = new URL(this.imageUrl);
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
        NotificationSettings settings = getNotificationSettings(result);

        Notification notification = this.notifcationFactory
                .createNotification(settings, this.mContext.get());
        NotificationManager notificationManager = (NotificationManager) this.mContext.get()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null)
            notificationManager.notify(this.id, notification);
    }

    private NotificationSettings getNotificationSettings(Bitmap result) {
        NotificationSettings settings = new NotificationSettings();
        settings.Title = title;
        settings.Body = content;
        settings.Ticker = ticker;
        settings.ExtraActions = extraActions.toArray(new NotificationAction[0]);
        settings.ContentIntent = this.onNotificationClick.getIntent(this.mContext.get());
        settings.IsOngoing = isOngoing;
        settings.LargeIcon = result;
        settings.Priority = importance;
        return settings;
    }
}