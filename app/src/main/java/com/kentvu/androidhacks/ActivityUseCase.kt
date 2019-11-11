package com.kentvu.androidhacks

import android.app.*
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.*
import kotlin.coroutines.suspendCoroutine

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0

/**
 * [UseCase] that sticks to this activity.
 */
class ActivityUseCase(private val activity: Activity) : UseCase {
    private val log = AndroidLog()

    val job = Job()
    val mainScope = CoroutineScope(job + Dispatchers.Main)
    override fun scheduleNotification(afterMillis: Int, fullScreen: Boolean) {
        mainScope.launch(Dispatchers.Main) {
            delayByAlarm(afterMillis)
            showServiceNotification(fullScreen)
        }
    }

    private suspend fun delayByAlarm(ms: Int):Unit = suspendCoroutine { cont ->
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = ContinuationService.makeContinuationIntent(activity, cont)
        val pendingService = PendingIntent.getService(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.set(
            AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + ms, pendingService
        )
    }

    private fun showServiceNotification(fullScreen: Boolean) {
        NotificationService.showNotification(activity, createNotification(fullScreen))
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

    override fun stopNotification() {
        NotificationService.cancelNotification(activity)
    }

    override fun showNotification() {
        NotificationManagerCompat.from(activity).notify(1, createNotification(true))
    }

    private fun createNotification(fullScreen: Boolean): Notification {
        // test https://developer.android.com/training/notify-user/time-sensitive
        val fullScreenIntent = MainActivity.getNotificationIntent(activity)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            activity, REQUEST_CODE,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder =
            NotificationCompat.Builder(activity, App.NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.ic_notification)
                .setContentTitle("Incoming call")
                .setContentText("(919) 555-1234")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
        if (fullScreen) {
            // Use a full-screen intent only for the highest-priority alerts where you
            // have an associated activity that you would like to launch after the user
            // interacts with the notification. Also, if your app targets Android 10
            // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
            // order for the platform to invoke this notification.
            notificationBuilder.setFullScreenIntent(fullScreenPendingIntent, true)
        } else {
            notificationBuilder
                .setAutoCancel(true)
                .setContentIntent(fullScreenPendingIntent)
        }
        val incomingCallNotification = notificationBuilder.build()
        return incomingCallNotification
    }

    override fun cancelNotification() {
        NotificationManagerCompat.from(activity).cancel(1)
    }
}
