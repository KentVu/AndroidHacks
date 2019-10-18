package com.kentvu.androidhacks

class DelegateUiEventConsumer(val evtConsumers: MutableList<UiPresenter.UiEvents>) :
    UiPresenter.UiEvents {
    override fun onActivityCreate(view: MainView) {
        // ugly implementation, but for the sake of clarity.
        // Could be better using reflection to extract out common functionality.
        // i.e using javaClass.enclosingMethod.name kind of.
        evtConsumers.forEach { it.onActivityCreate(view) }
    }

    override fun onRestartAppClick() {
        evtConsumers.forEach { it.onRestartAppClick() }
    }

    override fun onActivityDestroy(view: MainView) {
        evtConsumers.forEach { it.onActivityDestroy(view) }
    }

}
