package com.kentvu.androidhacks

import com.kentvu.common.Log

class UiPresenter(val view: View, val log: Log) {
    interface UiEvents {
        fun onActivityCreate()
        fun onRestartAppClick()
        fun onActivityDestroy()
        fun onNotificationClick()
    }

    interface BuildConfig {
        val variant: String
    }

    interface View {
        val build: BuildConfig
        var details: String

        fun finish()
        fun restart()
        fun createNotification()
    }

    val evtListener = object : UiEvents {
        override fun onActivityCreate() {
            log.w("CoreLogic.evt", "onActivityCreate:not implemented")
            view.details = "Build: ${view.build.variant}"
        }

        override fun onRestartAppClick() {
            log.d("CoreLogic.evt", "onRestartAppClick:not implemented")
            testRestartApp()
        }

        override fun onNotificationClick() {
            log.d("CoreLogic.evt", "onRestartAppClick:onNotificationClick")
            view.createNotification()
        }

        override fun onActivityDestroy() {
            log.d("CoreLogic.evt", "onActivityDestroy:not implemented")
        }
    }

    fun testRestartApp() {
        log.d("DefaultUiPresenter", "testRestartApp")
        view.restart()
    }
}
