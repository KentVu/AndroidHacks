package com.kentvu.androidhacks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter: DefaultUiPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.get(this).activityComponent.inject(this)
        presenter.evtSource.onActivityCreate()
    }

    fun onRestartAppClick(v: View) {
        presenter.evtSource.onRestartAppClick()
    }
}
