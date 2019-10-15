package com.kentvu.androidhacks

import android.util.Log
import com.kentvu.common.Log as CoreLog

class AndroidLog : CoreLog {
    override fun w(tag: String, msg: String) {
        Log.w(tag, msg)
    }

    override fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

}