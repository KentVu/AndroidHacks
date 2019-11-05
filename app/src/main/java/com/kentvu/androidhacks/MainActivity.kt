package com.kentvu.androidhacks

import android.app.ActivityManager
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.scope.currentScope
import org.koin.core.parameter.parametersOf

class MainActivity() : AppCompatActivity(), UiPresenter.View {

    override val build = object : UiPresenter.BuildConfig {
        override val variant = "${BuildConfig.FLAVOR} - ${BuildConfig.BUILD_TYPE}"
    }

    override var details: String
        get() = textView.text.toString()
        set(value) { textView.text = value }

    private val presenter : UiPresenter by currentScope.inject { parametersOf(this, ActivityUseCase(this)) }

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
        TODO()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.evtListener.onActivityDestroy()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onRestartAppClick(v: View) {
        presenter.evtListener.onRestartAppClick()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onNotificationClick(v: View) {
        presenter.evtListener.onNotificationClick()
    }
}

/**
 * [UseCase] that sticks to this activity.
 */
class ActivityUseCase(val activity: MainActivity) : UseCase {
    override fun scheduleNotification(afterMillis: Int) {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent("TODO", null, activity, MyService::class.java)
        val pendingService = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        } else {
            PendingIntent.getService(activity, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        alarmManager.set(
            AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + afterMillis, pendingService
        )
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

}
