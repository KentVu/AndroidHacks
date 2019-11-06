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
        fun onScheduleNotificationClick()
        fun onNotificationActivityCreate()
        fun onShowNotificationClick()
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
            log.w("UiPresenter.evt", "onActivityCreate")
            view.details = "Build: ${view.build.variant}"
            useCase.cancelNotification()
        }

        override fun onRestartAppClick() {
            log.d("UiPresenter.evt", "onRestartAppClick")
            testRestartApp()
        }

        override fun onScheduleNotificationClick() {
            log.d("UiPresenter.evt", "onRestartAppClick:onScheduleNotificationClick")
            useCase.scheduleNotification(2000)
            useCase.closeApp()
        }

        override fun onActivityDestroy() {
            log.d("UiPresenter.evt", "onActivityDestroy:not implemented")
        }

        override fun onNotificationActivityCreate() {
            useCase.stopNotification()
        }

        override fun onShowNotificationClick() {
            useCase.showNotification()
        }
    }

    fun testRestartApp() {
        log.d("DefaultUiPresenter", "testRestartApp")
        view.restart()
    }
}
