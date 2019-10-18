package com.kentvu.androidhacks

import com.kentvu.common.Log

class CoreLogic(val log: Log, val presenter: UiPresenter) {
    private val evtConsumer = object : UiPresenter.UiEvents {
        override fun onActivityCreate(view: MainView) {
            log.w("CoreLogic.evt", "onActivityCreate:not implemented")
        }

        override fun onRestartAppClick() {
            log.d("CoreLogic.evt", "onRestartAppClick:not implemented")
            presenter.testRestartApp()
        }

        override fun onActivityDestroy(view: MainView) {
            log.d("CoreLogic.evt", "onActivityDestroy:not implemented")
        }
    }
    init {
        presenter.subscribeEvent(evtConsumer)
    }
}
