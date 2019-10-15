package com.kentvu.androidhacks.dagger

import androidx.annotation.NonNull
import com.kentvu.androidhacks.AndroidLog
import com.kentvu.common.Log
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonModule {
    @Provides
    @Singleton
    @NonNull
    fun provideLog(): Log = AndroidLog()
}