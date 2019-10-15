package com.kentvu.androidhacks

import com.kentvu.common.Log

class CoreLogic(val log: Log, val presenter: UiPresenter) {
    private val evtConsumer = object : UiPresenter.UiEvents {
        override fun onActivityCreate() {
            log.w("CoreLogic.evt", "onActivityCreate:not implemented")
        }

        override fun onRestartAppClick() {
            log.d("CoreLogic.evt", "onRestartAppClick:not implemented")
            presenter.testRestartApp()
        }
    }
    init {
        presenter.subscribeEvent(evtConsumer)
    }
}
