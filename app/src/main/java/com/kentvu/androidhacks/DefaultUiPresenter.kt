package com.kentvu.androidhacks

import android.app.AlarmManager
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
    private val coreLogic = CoreLogic(AndroidLog(), this)
    private val delegateEventConsumer = DelegateUiEventConsumer(evtConsumers)
    private var mainView: MainView? = null
    val evtSource = object : UiEvents by delegateEventConsumer {
        override fun onActivityCreate(view: MainView) {
            delegateEventConsumer.onActivityCreate(view)
            this@DefaultUiPresenter.mainView = view
        }

        override fun onActivityDestroy(view: MainView) {
            delegateEventConsumer.onActivityDestroy(view)
            if (this@DefaultUiPresenter.mainView == view)
                mainView = null
        }
    }

    override fun testRestartApp() {
        log.d("DefaultUiPresenter", "testRestartApp:view!=null?${mainView!=null}")
        //val alarmManager = app.getSystemService<AlarmManager>()
//        alarmManager?.set(AlarmManager.RTC_WAKEUP,
//            System.currentTimeMillis() + 500,
//            PendingIntent.getActivity(app, 0,
//                Intent(app, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
//        mainView?.finish()
        mainView?.restart()
    }
}
