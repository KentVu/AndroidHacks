package com.kentvu.androidhacks

import com.kentvu.androidhacks.UiPresenter.UiEvents

class DefaultUiPresenter : UiPresenter {
    override fun subscribeEvent(evtConsumer: UiEvents) {
        evtConsumers.add(evtConsumer)
    }

    private val evtConsumers = mutableListOf<UiEvents>() // a little bit ugly naming but...

    // need to run CoreLogic.init
    @Suppress("unused")
    val coreLogic = CoreLogic(AndroidLog(), this)
    val evtSource = object : UiEvents by DelegateUiEventConsumer(evtConsumers) { }
}
