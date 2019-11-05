package com.kentvu.androidhacks

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.scope.currentScope
import org.koin.core.parameter.parametersOf

class MainActivity() : AppCompatActivity(), UiPresenter.View {

    override val build = object : UiPresenter.BuildConfig {
        override val variant = "${BuildConfig.FLAVOR} - ${BuildConfig.BUILD_TYPE}"
    }

    override var details: String
        get() = textView.text.toString()
        set(value) { textView.text = value }

    private val presenter : UiPresenter by currentScope.inject { parametersOf(this, ActivityUseCase(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //recycler.adapter = adapter
        presenter.evtListener.onActivityCreate()
    }

    override fun restart() {
        //recreate()
        finish()
        startActivity(intent)
    }

    override fun createNotification() {
        TODO()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.evtListener.onActivityDestroy()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onRestartAppClick(v: View) {
        presenter.evtListener.onRestartAppClick()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onNotificationClick(v: View) {
        presenter.evtListener.onNotificationClick()
    }
}

