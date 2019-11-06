package com.kentvu.androidhacks

interface UseCase {
    fun scheduleNotification(afterMillis: Int)
    fun closeApp()
    fun stopNotification()
    fun showNotification()
    fun cancelNotification()
}
