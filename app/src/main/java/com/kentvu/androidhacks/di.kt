package com.kentvu.androidhacks

import com.kentvu.common.Log
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    // single instance of HelloRepository
    single<Log> { AndroidLog() }

    // Simple Presenter Factory
    //factory { UiPresenter(view) }
    scope(named<MainActivity>()) {
        scoped { (view: UiPresenter.View) -> UiPresenter(view, get()) } // TODO inject log
    }
}

val presentersModule = module {
}
