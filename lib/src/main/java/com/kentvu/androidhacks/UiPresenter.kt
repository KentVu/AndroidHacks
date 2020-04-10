package com.kentvu.androidhacks

import com.kentvu.common.Log

class UiPresenter(
    val view: View,
    val log: Log,
    private val useCase: UseCase
) {
    interface UiEvents {
        fun onActivityCreate()
        fun onMainFragmentCreate()
        fun onActivityDestroy()
        fun onNotificationActivityCreate()
        fun onBtnGoClick(hack: String)
    }

    interface BuildConfig {
        val variant: String
    }

    interface View {
        val isNotifFullScreen: Boolean
        val build: BuildConfig
        var details: String

        fun populateHacks(hacks: List<String>)
    }

    val evtListener = object : UiEvents {
        override fun onActivityCreate() {
            log.w("UiPresenter.evt", "onActivityCreate")
            useCase.cancelNotification()
        }

        override fun onBtnGoClick(hack: String) {
            val fullScreen = view.isNotifFullScreen
            if (hack == UseCase::scheduleNotification.name) {
                log.d("UiPresenter.evt", "$hack:fs=$fullScreen")
                useCase.scheduleNotification(2000, fullScreen)
                useCase.closeApp()
            } else {
                log.d("UiPresenter.evt", "onBtnGoClick:hack=$hack")
                useCase.invoke(hack)
            }
        }

        override fun onActivityDestroy() {
            log.d("UiPresenter.evt", "onActivityDestroy:not implemented")
        }

        override fun onNotificationActivityCreate() {
            useCase.stopNotification()
        }

        override fun onMainFragmentCreate() {
            view.details = "Build: ${view.build.variant}"
            view.populateHacks(useCase.hacks)
        }
    }
}
