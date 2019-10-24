package com.kentvu.androidhacks

import com.kentvu.common.Log

class UiPresenter(val view: View, val log: Log) {
    interface UiEvents {
        fun onActivityCreate()
        fun onRestartAppClick()
        fun onActivityDestroy()
    }

    interface View {
        fun finish()
        fun restart()
    }

    val evtListener = object : UiEvents {
        override fun onActivityCreate() {
            log.w("CoreLogic.evt", "onActivityCreate:not implemented")
        }

        override fun onRestartAppClick() {
            log.d("CoreLogic.evt", "onRestartAppClick:not implemented")
        }

        override fun onActivityDestroy() {
            log.d("CoreLogic.evt", "onActivityDestroy:not implemented")
        }
    }

    fun testRestartApp() {
        log.d("DefaultUiPresenter", "testRestartApp:view!=null?${view!=null}")
        //val alarmManager = app.getSystemService<AlarmManager>()
//        alarmManager?.set(AlarmManager.RTC_WAKEUP,
//            System.currentTimeMillis() + 500,
//            PendingIntent.getActivity(app, 0,
//                Intent(app, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
//        mainView?.finish()
        view.restart()
    }
}
