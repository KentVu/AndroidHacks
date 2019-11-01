package com.kentvu.androidhacks

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.scope.currentScope
import org.koin.core.parameter.parametersOf

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0

class MainActivity() : AppCompatActivity(), UiPresenter.View {
    private val NOTIFICATION_CHANNEL: String by lazy {
        val channelId = "Notification_ChannelId_Kien"
        createNotificationChannel(channelId)
        channelId
    }

    private fun createNotificationChannel(channelId: String) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override val build = object : UiPresenter.BuildConfig {
        override val variant = "${BuildConfig.FLAVOR} - ${BuildConfig.BUILD_TYPE}"
    }

    override var details: String
        get() = textView.text.toString()
        set(value) { textView.text = value }

    private val presenter : UiPresenter by currentScope.inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //recycler.adapter = adapter
        presenter.evtListener.onActivityCreate()
    }

    override fun restart() {
        //recreate()
        finish()
        startActivity(intent)
    }

    override fun createNotification() {
        // test https://developer.android.com/training/notify-user/time-sensitive
        val fullScreenIntent = Intent(this, NotificationActivity::class.java)
        val fullScreenPendingIntent = PendingIntent.getActivity(
            this, REQUEST_CODE,
            fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.mipmap.ic_notification)
                .setContentTitle("Incoming call")
                .setContentText("(919) 555-1234")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)

                // Use a full-screen intent only for the highest-priority alerts where you
                // have an associated activity that you would like to launch after the user
                // interacts with the notification. Also, if your app targets Android 10
                // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                // order for the platform to invoke this notification.
                .setFullScreenIntent(fullScreenPendingIntent, true)

        val incomingCallNotification = notificationBuilder.build()
// Provide a unique integer for the "notificationId" of each notification.
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(NOTIFICATION_ID, incomingCallNotification)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.evtListener.onActivityDestroy()
    }

    fun onRestartAppClick(v: View) {
        presenter.evtListener.onRestartAppClick()
    }

    fun onNotificationClick(v: View) {
        presenter.evtListener.onNotificationClick()
    }
}
