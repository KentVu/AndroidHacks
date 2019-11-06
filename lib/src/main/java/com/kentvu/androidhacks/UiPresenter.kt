package com.kentvu.androidhacks

import com.kentvu.common.Log

class UiPresenter(
    val view: View,
    val log: Log,
    private val useCase: UseCase
) {
    interface UiEvents {
        fun onActivityCreate()
        fun onRestartAppClick()
        fun onActivityDestroy()
        fun onNotificationClick()
        fun onNotificationActivityCreate()
    }

    interface BuildConfig {
        val variant: String
    }

    interface View {
        val build: BuildConfig
        var details: String

        fun finish()
        fun restart()
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
            useCase.scheduleNotification(2000)
            useCase.closeApp()
        }

        override fun onActivityDestroy() {
            log.d("CoreLogic.evt", "onActivityDestroy:not implemented")
        }

        override fun onNotificationActivityCreate() {
            useCase.stopNotification()
        }
    }

    fun testRestartApp() {
        log.d("DefaultUiPresenter", "testRestartApp")
        view.restart()
    }
}
