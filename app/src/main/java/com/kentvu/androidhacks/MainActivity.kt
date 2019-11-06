package com.kentvu.androidhacks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.scope.currentScope
import org.koin.core.parameter.parametersOf

class MainActivity() : AppCompatActivity(), UiPresenter.View {

    companion object {
        private val ACTION_NOTIFICATION = "NOTIFICATION"
        fun getNotificationIntent(ctx: Context) = Intent(ACTION_NOTIFICATION, null, ctx, MainActivity::class.java)
    }
    override val build = object : UiPresenter.BuildConfig {
        override val variant = "${BuildConfig.FLAVOR} - ${BuildConfig.BUILD_TYPE}"
    }

    override var details: String
        get() = textView.text.toString()
        set(value) { textView.text = value }

    private val presenter : UiPresenter by currentScope.inject { parametersOf(this, ActivityUseCase(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent == null || intent.action == Intent.ACTION_MAIN) {
            setContentView(R.layout.activity_main)
            presenter.evtListener.onActivityCreate()
        } else { /*ACTION_NOTIFICATION*/
            setContentView(R.layout.activity_notification)
            // TODO: Cancel notification
            presenter.evtListener.onNotificationActivityCreate()
        }
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

