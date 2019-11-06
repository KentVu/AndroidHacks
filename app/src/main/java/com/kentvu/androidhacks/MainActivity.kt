package com.kentvu.androidhacks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.scope.currentScope
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainActivity() : AppCompatActivity(), UiPresenter.View {

    companion object {
        private val ACTION_NOTIFICATION = "NOTIFICATION"
        fun getNotificationIntent(ctx: Context) = Intent(ACTION_NOTIFICATION, null, ctx, MainActivity::class.java)
    }
    override val build = object : UiPresenter.BuildConfig {
        override val variant = "${BuildConfig.FLAVOR} - ${BuildConfig.BUILD_TYPE}"
    }

    private val mainFragment = Fragment(R.layout.content_main)
    private val notificationFragment = NotificationFragment()

    override var details: String
        get() = mainFragment.textView.text.toString()
        set(value) { mainFragment.textView.text = value }

    private val presenter : UiPresenter by currentScope.inject { parametersOf(this, ActivityUseCase(this)) }
    private lateinit var mainScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        log.d("MainActivity", "onResume")
        mainScope = MainScope()
        if (intent == null || intent.action == Intent.ACTION_MAIN) {
            mainScope.launch {
                switchFragment(mainFragment)
                presenter.evtListener.onActivityCreate()
            }
        } else { /*ACTION_NOTIFICATION*/
            supportFragmentManager.beginTransaction().add(R.id.root_layout, notificationFragment).commit()
            presenter.evtListener.onNotificationActivityCreate()
        }
    }

    override fun onPause() {
        super.onPause()
        log.d("MainActivity", "onPause")
        mainScope.cancel()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        log.d("MainActivity", "onSaveInstanceState:outState=[$outState],oPS=[$outPersistentState]")
        // mainScope.cancel()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.findFragmentById(R.id.root_layout) == mainFragment) {
            supportFragmentManager.beginTransaction().replace(R.id.root_layout, mainFragment).commit()
        } else {
            super.onBackPressed()
        }
    }

    override fun restart() {
        //recreate()
        finish()
        startActivity(intent)
    }

    private suspend fun switchFragment(fragment: Fragment) {
        log.d("MainActivity", "switchFragment:to[$fragment]resumed=TODO")
        supportFragmentManager.beginTransaction().replace(R.id.root_layout, fragment).commit()
        waitForFragmentViewCreated()
    }

    private suspend fun waitForFragmentViewCreated() = suspendCoroutine {cont: Continuation<Unit> ->
        supportFragmentManager.registerFragmentLifecycleCallbacks(object :
            FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
                super.onFragmentViewCreated(fm, f, v, savedInstanceState)
                supportFragmentManager.unregisterFragmentLifecycleCallbacks(this)
                cont.resume(Unit)
            }
        }, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.evtListener.onActivityDestroy()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onRestartAppClick(v: View) {
        presenter.evtListener.onRestartAppClick()
    }

    @Suppress("UNUSED_PARAMETER")
    fun onNotificationClick(v: View) {
        presenter.evtListener.onNotificationClick()
    }
}

class MainFragment: Fragment(R.layout.content_main) {
}
class NotificationFragment: Fragment(R.layout.content_notification)