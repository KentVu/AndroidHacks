package com.kentvu.androidhacks.dagger

import androidx.annotation.NonNull
import com.kentvu.androidhacks.DefaultUiPresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @Provides
    //@Singleton
    @NonNull
    fun providePresenter(): DefaultUiPresenter = DefaultUiPresenter()
}
