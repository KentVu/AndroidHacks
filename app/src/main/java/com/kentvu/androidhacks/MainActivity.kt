package com.kentvu.androidhacks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainView {
    @Inject
    lateinit var presenter: DefaultUiPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.get(this).activityComponent.inject(this)
        presenter.evtSource.onActivityCreate(this)
    }

    override fun restart() {
        //recreate()
        finish()
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.evtSource.onActivityDestroy(this)
    }

    fun onRestartAppClick(v: View) {
        presenter.evtSource.onRestartAppClick()
    }
}
