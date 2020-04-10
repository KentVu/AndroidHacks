package com.kentvu.androidhacks

import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.time.ExperimentalTime

interface UseCase {
    @Target(AnnotationTarget.FUNCTION)
    annotation class UseCaseFunction

    @OptIn(ExperimentalStdlibApi::class)
    val hacks: List<String> get() {
        return UseCase::class.memberFunctions.filter {
            it.hasAnnotation<UseCaseFunction>()
        }.map { it.name }.toList()
    }

    fun invoke(hack: String, vararg args: Any?) {
        UseCase::class.memberFunctions.first { it.name == hack }.run {
            call(this@UseCase, *args)
            // if (args.isEmpty())
            //     call(this)
            // else
            //     call(this, args)
        }
    }

    @UseCaseFunction
    fun scheduleNotification(afterMillis: Int, fullScreen: Boolean)
    @UseCaseFunction
    fun closeApp()
    @UseCaseFunction
    fun stopNotification()
    @UseCaseFunction
    fun showNotification()
    @UseCaseFunction
    fun cancelNotification()
    @UseCaseFunction
    fun testRestartApp()
}
