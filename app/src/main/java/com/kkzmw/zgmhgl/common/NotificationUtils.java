package com.kkzmw.zgmhgl.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.RemoteViews;

import com.kkzmw.zgmhgl.R;

/**
 * Created by chencheng on 14-10-11.
 */
public class NotificationUtils {
    public static void notify(Context cxt, String title, String message, int flags, String ticker, PendingIntent pi, int notificationId){

    RemoteViews views = null;
        views = new RemoteViews(cxt.getPackageName(),
                R.layout.dx_notification_two_line_text);
    views.setImageViewResource(R.id.icon, R.drawable.icon);
    views.setTextViewText(R.id.title, title);
    views.setTextViewText(R.id.message, message);
    Notification notification = new Notification();
    notification.flags = flags;
    notification.icon = R.drawable.icon;
    notification.tickerText = ticker;
    notification.contentView = views;
    notification.contentIntent = pi;
    notification.when = 0;
    notification.defaults = 0;
    notification.sound = null;
    notification.vibrate = null;

    NotificationManager nm = (NotificationManager) cxt.getSystemService(
            Context.NOTIFICATION_SERVICE);
    nm.notify(notificationId, notification);
    }
}
