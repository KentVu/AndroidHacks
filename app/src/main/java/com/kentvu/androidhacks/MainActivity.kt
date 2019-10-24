package com.kentvu.androidhacks

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.scope.currentScope
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity(), UiPresenter.View {

    private val presenter : UiPresenter by currentScope.inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter.evtListener.onActivityCreate()
    }

    override fun restart() {
        //recreate()
        finish()
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.evtListener.onActivityDestroy()
    }

    fun onRestartAppClick(v: View) {
        presenter.evtListener.onRestartAppClick()
    }
}
