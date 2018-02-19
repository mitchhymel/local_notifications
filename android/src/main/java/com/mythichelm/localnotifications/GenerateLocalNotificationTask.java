package com.mythichelm.localnotifications;

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

public class GenerateLocalNotificationTask extends AsyncTask<String, Void, Bitmap> {
    private Context mContext;
    private int id;
    private String title, content, imageUrl, ticker;
    private int importance;
    private boolean isOngoing;

    public GenerateLocalNotificationTask(Context context, int id, String title, String content,
                                         String imageUrl, String ticker, int importance, boolean isOngoing) {
        super();
        this.mContext = context;
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.ticker = ticker;
        this.importance = importance;
        this.isOngoing = isOngoing;
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

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        try {
            Intent notificationIntent = this.mContext
                    .getPackageManager()
                    .getLaunchIntentForPackage(this.mContext.getPackageName());

            PendingIntent contentIntent = PendingIntent.getActivity(this.mContext, 0, notificationIntent, 0);

            Drawable drawable = this.mContext
                    .getPackageManager()
                    .getApplicationIcon(this.mContext.getPackageName());

            Icon icon = Icon.createWithBitmap(((BitmapDrawable) drawable).getBitmap());

            Notification.Builder builder = new Notification.Builder(this.mContext)
                    .setContentIntent(contentIntent)
                    .setSmallIcon(icon)
                    .setContentTitle(this.title)
                    .setContentText(this.content)
                    .setOngoing(this.isOngoing);

            if (result != null) {
                builder.setLargeIcon(result);
            }

            if (this.ticker != null) {
                builder.setTicker(this.ticker);
            }

            Notification notification = builder.build();

            NotificationManager notificationManager =
                    (NotificationManager) this.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(this.id, notification);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}