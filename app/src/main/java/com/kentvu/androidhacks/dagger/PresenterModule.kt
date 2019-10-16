package com.kentvu.androidhacks.dagger

import androidx.annotation.NonNull
import com.kentvu.androidhacks.App
import com.kentvu.androidhacks.DefaultUiPresenter
import com.kentvu.androidhacks.MainActivity
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PresenterModule(private val app: App) {
    @Provides
    @Singleton
    @NonNull
    fun providePresenter(activity: MainActivity): DefaultUiPresenter = DefaultUiPresenter(app, activity)
}
