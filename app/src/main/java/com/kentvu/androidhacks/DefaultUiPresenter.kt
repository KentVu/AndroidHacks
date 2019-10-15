package com.kentvu.androidhacks

import com.kentvu.androidhacks.CoreLogic.UiPresenter.UiEvents

class DefaultUiPresenter : CoreLogic.UiPresenter {
    val coreLogic = CoreLogic(AndroidLog(), this)
    override val evtSource: UiEvents = object : UiEvents by coreLogic.evtConsumer{ }
}