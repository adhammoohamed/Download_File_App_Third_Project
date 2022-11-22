package com.udacity.util

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.MainActivity
import com.udacity.R

val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    val contentIntent= Intent(applicationContext, DetailActivity::class.java)

    // to share data between activities using putExtra
    contentIntent.putExtra("fileName", MainActivity.fileName)
    contentIntent.putExtra("status", MainActivity.downloadStatus)

    // using pendingIntent to go to detail activity if the is running or not
    val contentPendingIntent=PendingIntent.getActivity(applicationContext,
        NOTIFICATION_ID,contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .addAction(
         R.drawable.ic_dm,
         applicationContext.getString(R.string.check_download),
         contentPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_LOW)

    notify(NOTIFICATION_ID , builder.build())
}