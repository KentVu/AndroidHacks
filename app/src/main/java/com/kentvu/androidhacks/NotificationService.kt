package com.kentvu.androidhacks

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

class NotificationService : Service() {
    companion object {
        private const val ACTION_SHOW_NOTIFICATION = "show_notification"
        private const val ACTION_CANCEL_NOTIFICATION = "cancel_notification"
        private const val EXTRA_NOTI = "noti"

        fun showNotification(ctx: Context, notification: Notification) {
            ctx.startService(
                Intent(ACTION_SHOW_NOTIFICATION, null, ctx, NotificationService::class.java).apply {
                    putExtra(EXTRA_NOTI, notification)
                })
        }

        fun cancelNotification(ctx: Context) {

            ctx.startService(Intent(ACTION_CANCEL_NOTIFICATION, null, ctx, NotificationService::class.java))
        }

    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log.d("NotificationService", "onStartCommand:$intent")
        intent ?: return START_NOT_STICKY
        when (intent.action) {
            ACTION_SHOW_NOTIFICATION -> {
                startForeground(1, intent.getParcelableExtra(EXTRA_NOTI))
            }
            else -> { /*ACTION_CANCEL_NOTIFICATION*/
                stopForeground(true)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
