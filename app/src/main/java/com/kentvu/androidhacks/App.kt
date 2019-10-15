package com.kentvu.androidhacks

import android.app.Application
import android.content.Context
import com.kentvu.androidhacks.dagger.ActivityComponent
import com.kentvu.androidhacks.dagger.DaggerActivityComponent
import com.kentvu.androidhacks.dagger.PresenterModule

class App: Application() {
    companion object {
        fun get(ctx: Context) = ctx.applicationContext as App
    }

    val activityComponent: ActivityComponent by lazy {
        buildActivityComponent()
    }

    private fun buildActivityComponent(): ActivityComponent {
        return DaggerActivityComponent.builder().presenterModule(PresenterModule()).build()
    }
}