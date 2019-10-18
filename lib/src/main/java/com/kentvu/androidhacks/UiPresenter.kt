package com.kentvu.androidhacks

interface UiPresenter {
    fun subscribeEvent(evtConsumer: UiEvents)

    fun testRestartApp()

    interface UiEvents {
        fun onActivityCreate(view: MainView)
        fun onRestartAppClick()
        fun onActivityDestroy(view: MainView)
    }
}

interface MainView {
    fun finish()
    fun restart()
}
