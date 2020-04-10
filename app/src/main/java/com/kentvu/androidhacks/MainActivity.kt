package com.kentvu.androidhacks

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
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

    override val isNotifFullScreen: Boolean
        get() = spinNotifType.selectedItem == resources.getStringArray(R.array.notification_type)[0]

    override val build = object : UiPresenter.BuildConfig {
        override val variant = "${BuildConfig.FLAVOR} - ${BuildConfig.BUILD_TYPE}"
    }

    internal val presenter : UiPresenter by currentScope.inject { parametersOf(this, ActivityUseCase(this)) }

    private val mainFragment = MainFragment()
    private val notificationFragment = Fragment(R.layout.content_notification)

    override var details: String
        get() = mainFragment.textView.text.toString()
        set(value) { mainFragment.textView.text = value }

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
                switchAndWaitFragment(mainFragment)
                presenter.evtListener.onActivityCreate()
            }
        } else { /*ACTION_NOTIFICATION*/
            switchFragment(notificationFragment)
            presenter.evtListener.onNotificationActivityCreate()
            //intent = null
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
        if (supportFragmentManager.findFragmentById(R.id.root_layout) == notificationFragment) {
            supportFragmentManager.beginTransaction().replace(R.id.root_layout, mainFragment).commit()
        } else {
            super.onBackPressed()
        }
    }

    override fun populateHacks(hacks: List<String>) {
        mainFragment.populateHacks(hacks)
    }

    private fun switchFragment(fragment: Fragment) {
        log.d("MainActivity", "switchFragment:to[$fragment]")
        supportFragmentManager.beginTransaction().replace(R.id.root_layout, fragment).commit()
    }

    private suspend fun switchAndWaitFragment(fragment: Fragment) {
        log.d("MainActivity", "switchAndWaitFragment:to[$fragment]")
        switchFragment(fragment)
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
    fun onGoClick(v: View) {
        presenter.evtListener.onBtnGoClick(mainFragment.currentHack)
    }
}

class MainFragment : Fragment(R.layout.content_main) {
    private lateinit var hacksAdapter: SpinnerAdapter
    val currentHack: String get() = spinHacks.selectedItem as String

    private val presenter: UiPresenter by lazy {
        (activity as MainActivity).presenter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateSpinners()
        presenter.evtListener.onMainFragmentCreate()
    }

    /** Create an ArrayAdapter using the string array and a default spinner layout. */
    private fun populateSpinners() {
        // Apply the adapter to the spinner
        spinNotifType.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.notification_type,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinNotifPriority.adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.notification_priority,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    fun populateHacks(hacks: List<String>) {
        hacksAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            hacks
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinHacks.adapter = hacksAdapter
    }
}
class NotificationFragment: Fragment(R.layout.content_notification)