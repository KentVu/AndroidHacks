package com.kentvu.androidhacks

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.util.Log


class ContinuationService : Service() {

    companion object {
        private const val CONTINUATION_EXTRA = "cont"
        private const val ACTION_SHOW_NOTIFICATION = "show_notification"
        private const val EXTRA_NOTI = "noti"
        private const val ACTION_CONTINUATION = "continue"

        internal fun Intent.putContinuation(messenger: Messenger) {
            putExtra(CONTINUATION_EXTRA, messenger)
        }

        internal fun Intent.getContinuation(): Messenger {
            return getParcelableExtra(CONTINUATION_EXTRA) ?: throw NullPointerException("No CONTINUATION_EXTRA in intent!")
        }


        fun showNotification(ctx: Context, notification: Notification) {
            ctx.startService(
                Intent(ACTION_SHOW_NOTIFICATION, null, ctx, ContinuationService::class.java).apply {
                    putExtra(EXTRA_NOTI, notification)
                })
        }

        fun makeContinuationIntent(ctx: Context, handler: Handler): Intent =
            Intent(ACTION_CONTINUATION, null, ctx, ContinuationService::class.java).apply {
                putContinuation(Messenger(handler))
            }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ContinuationService", "onStartCommand:$intent")
        // startForeground(1, createNotification())
        intent ?: return START_NOT_STICKY
        when (intent.action) {
            ACTION_CONTINUATION -> {
                intent.getContinuation().send(Message.obtain())
            }
            else -> { /*ACTION_SHOW_NOTIFICATION*/
                startForeground(1, intent.getParcelableExtra(EXTRA_NOTI))
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
