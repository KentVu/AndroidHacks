package com.kentvu.androidhacks

interface UiPresenter {
    fun subscribeEvent(evtConsumer: UiEvents)

    fun testRestartApp()

    interface UiEvents {
        fun onActivityCreate()
        fun onRestartAppClick()
    }
}