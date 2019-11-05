package com.kentvu.androidhacks

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0

/**
 * [UseCase] that sticks to this activity.
 */
class ActivityUseCase(val activity: MainActivity) : UseCase {
    private val log = AndroidLog()
    val job = Job()
    val mainScope = CoroutineScope(job + Dispatchers.Main)
    override fun scheduleNotification(afterMillis: Int) {
        mainScope.launch(Dispatchers.Main) {
            delayByAlarm(afterMillis)
            showNotification()
        }
    }

    override fun closeApp() {
        val activityManager = activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (appTask in activityManager.appTasks) {
                appTask.finishAndRemoveTask()
            }
        } else {
            activity.finish()
        }
    }


    private suspend fun delayByAlarm(ms: Int):Unit = suspendCoroutine { cont ->
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = ContinuationService.makeContinuationIntent(activity,
            object : Handler() {
                override fun handleMessage(msg: Message) {
                    log.d("Handler", "$msg")
                    cont.resume(Unit)
                }
            })
        val pendingService = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getService(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        alarmManager.set(
            AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ms, pendingService
        )
    }

    fun createNotification(): Notification {
        // test https://developer.android.com/training/notify-user/time-sensitive
        val fullScreenIntent = Intent(activity, NotificationActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            activity, REQUEST_CODE,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder =
            NotificationCompat.Builder(activity, App.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.ic_notification)
                .setContentTitle("Incoming call")
                .setContentText("(919) 555-1234")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)

                // Use a full-screen intent only for the highest-priority alerts where you
                // have an associated activity that you would like to launch after the user
                // interacts with the notification. Also, if your app targets Android 10
                // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                // order for the platform to invoke this notification.
                .setFullScreenIntent(fullScreenPendingIntent, true)

        val incomingCallNotification = notificationBuilder.build()
        return incomingCallNotification
    }

    private fun showNotification() {
        ContinuationService.showNotification(activity, createNotification())
    }

}
