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
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume



class ContinuationService : Service() {

    companion object {
        private const val CONTINUATION_EXTRA = "cont"
        private const val ACTION_CONTINUATION = "continue"

        internal fun Intent.putContinuation(messenger: Messenger) {
            putExtra(CONTINUATION_EXTRA, messenger)
        }

        internal fun Intent.getContinuation(): Messenger {
            return getParcelableExtra(CONTINUATION_EXTRA) ?: throw NullPointerException("No CONTINUATION_EXTRA in intent!")
        }


        fun makeContinuationIntent(ctx: Context, cont: Continuation<Unit>): Intent =
            Intent(ACTION_CONTINUATION, null, ctx, ContinuationService::class.java).apply {
                putContinuation(Messenger(ContinuationHandler(cont)))
            }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log.d("ContinuationService", "onStartCommand:$intent")
        intent ?: return START_NOT_STICKY
        when (intent.action) {
            ACTION_CONTINUATION -> {
                intent.getContinuation().send(Message.obtain())
            }
            else -> {

            }
        }
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

class ContinuationHandler(private val cont: Continuation<Unit>) : Handler() {
    override fun handleMessage(msg: Message) {
        log.d("Handler", "$msg")
        cont.resume(Unit)
    }
}
