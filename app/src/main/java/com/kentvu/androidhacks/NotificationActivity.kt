package com.kentvu.androidhacks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.koin.android.scope.currentScope
import org.koin.core.parameter.parametersOf

class NotificationActivity : AppCompatActivity(),UiPresenter.View {
    override val build: UiPresenter.BuildConfig
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override var details: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}

    override fun restart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createNotification() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val presenter : UiPresenter by currentScope.inject { parametersOf(this, ActivityUseCase(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        // TODO: Cancel notification
        presenter.evtListener.onNotificationActivityCreate()
    }
}
