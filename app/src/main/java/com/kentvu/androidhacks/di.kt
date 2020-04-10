package com.kentvu.androidhacks

import com.kentvu.common.Log
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single<Log> { AndroidLog() }

    // Simple Presenter Factory
    scope(named<MainActivity>()) {
        scoped { (view: UiPresenter.View, useCase: UseCase) -> UiPresenter(view, get(), useCase) }
    }
}

val presentersModule = module {
}
