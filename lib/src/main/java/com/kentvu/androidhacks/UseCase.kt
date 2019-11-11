package com.kentvu.androidhacks

interface UseCase {
    fun scheduleNotification(afterMillis: Int, fullScreen: Boolean)
    fun closeApp()
    fun stopNotification()
    fun showNotification()
    fun cancelNotification()
}
