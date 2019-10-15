package com.kentvu.androidhacks

import com.kentvu.common.Log

class CoreLogic(val log: Log, val presenter: UiPresenter) {
    val evtConsumer = object : UiPresenter.UiEvents {
        override fun onActivityCreate() {
            log.w("CoreLogic.evt", "onActivityCreate:not implemented")
        }

    }

    interface UiPresenter {
        val evtSource: UiEvents

        interface UiEvents {
            fun onActivityCreate()

        }
    }
}
