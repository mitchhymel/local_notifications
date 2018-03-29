package com.mythichelm.localnotifications;

import android.app.NotificationChannel;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import android.app.Notification;
import android.app.Notification.*;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import java.io.*;
import java.net.*;
import android.content.pm.PackageManager;
import java.util.List;

public class GenerateLocalNotificationsTask extends AsyncTask<String, Void, Bitmap> {
    private Context mContext;
    private int id;
    private String title, content, imageUrl, ticker;
    private int importance;
    private boolean isOngoing;
    private NotificationAction onNotificationClick;
    private List<NotificationAction> extraActions;

    public GenerateLocalNotificationsTask(Context context, int id, String title, String content,
                                          String imageUrl, String ticker, int importance, boolean isOngoing,
                                          NotificationAction onNotificationClick,
                                          List<NotificationAction> extraActions) {
        super();
        this.mContext = context;
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.ticker = ticker;
        this.importance = importance;
        this.isOngoing = isOngoing;
        this.onNotificationClick = onNotificationClick;
        this.extraActions = extraActions;
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
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private PendingIntent getIntentForAction(NotificationAction action, int id) {
        Intent actionIntent;
        if (action.launchesApp) {
            actionIntent = this.mContext
                    .getPackageManager()
                    .getLaunchIntentForPackage(this.mContext.getPackageName());
        }
        else {
            actionIntent = new Intent(this.mContext, LocalNotificationsService.class);
        }

        // if its not an empty action, then it has a callback and payload
        if (!action.isEmptyAction()) {
            actionIntent.putExtra(LocalNotificationsPlugin.CALLBACK_KEY, action.callbackFunctionName);
            actionIntent.putExtra(LocalNotificationsPlugin.PAYLOAD_KEY, action.intentPayload);
        }

        PendingIntent resultIntent;
        if (action.launchesApp) {
            resultIntent= PendingIntent.getActivity(this.mContext, id,
                    actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        else {
            resultIntent= PendingIntent.getService(this.mContext, id,
                    actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        return resultIntent;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        try {
            // Get this app's icon
            Drawable drawable = this.mContext
                    .getPackageManager()
                    .getApplicationIcon(this.mContext.getPackageName());

            android.graphics.drawable.
            Icon icon = Icon.createWithBitmap(((BitmapDrawable) drawable).getBitmap());

            int nextNotificationId = 0;
            PendingIntent onClickNotificationIntent = getIntentForAction(this.onNotificationClick, nextNotificationId++);

            Notification.Builder builder = new Notification.Builder(this.mContext)
                    .setContentIntent(onClickNotificationIntent)
                    .setSmallIcon(icon)
                    .setContentTitle(this.title)
                    .setContentText(this.content)
                    .setOngoing(this.isOngoing);

            // if an image was provided, result will contain the loaded image
            if (result != null) {
                builder.setLargeIcon(result);
            }

            if (this.ticker != null) {
                builder.setTicker(this.ticker);
            }

            // if any extra actions were provided
            for (NotificationAction extraAction : this.extraActions) {
                PendingIntent intent = getIntentForAction(extraAction, nextNotificationId++);
                Action action = new Notification.Action.Builder(icon, extraAction.actionText, intent).build();
                builder.addAction(action);
            }

            builder.setVibrate(new long[] {1000, 1000, 1000});
            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            builder.setPriority(Notification.PRIORITY_HIGH);

            Notification notification = builder.build();

            NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(this.id, notification);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}