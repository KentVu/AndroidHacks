package com.kentvu.androidhacks.dagger

import com.kentvu.androidhacks.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [CommonModule::class])
interface CommonComponent {
}