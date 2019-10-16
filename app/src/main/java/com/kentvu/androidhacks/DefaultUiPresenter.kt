package com.kentvu.androidhacks

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.content.getSystemService
import com.kentvu.androidhacks.UiPresenter.UiEvents

class DefaultUiPresenter(private val app: App) : UiPresenter {
    override fun subscribeEvent(evtConsumer: UiEvents) {
        evtConsumers.add(evtConsumer)
    }

    private val log = AndroidLog()
    private val evtConsumers = mutableListOf<UiEvents>() // a little bit ugly naming but...

    // need to run CoreLogic.init
    @Suppress("unused")
    val coreLogic = CoreLogic(AndroidLog(), this)
    val evtSource = object : UiEvents by DelegateUiEventConsumer(evtConsumers) { }

    override fun testRestartApp() {
        log.d("DefaultUiPresenter", "testRestartApp")
        val alarmManager = app.getSystemService<AlarmManager>()
        alarmManager?.set(AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 500,
            PendingIntent.getActivity(app, 0,
                Intent(app, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
    }
}
