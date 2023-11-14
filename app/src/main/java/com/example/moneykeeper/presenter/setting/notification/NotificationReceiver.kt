package com.example.moneykeeper.presenter.setting.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.moneykeeper.R

class NotificationReceiver : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "MYCHANNEL"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "abc", Toast.LENGTH_SHORT).show()
        // Create and show the notification
        val builder = context?.let {
            NotificationCompat.Builder(it, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.noti_title))
                .setContentText(context.getString(R.string.noti_conent))
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        }

        val manager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (builder != null) {
            manager.notify(NOTIFICATION_ID, builder.build())
        }
    }

}