package zms.song.bore.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import zms.song.bore.BaseApplication

/**
 * @author: Listensong
 * Time: 19-10-21 下午8:20
 * Desc: zms.song.bore.base.BaseFragment
 */
abstract class BaseFragment : Fragment() {
    companion object {
        var TAG = "BaseFragment"
    }

    protected lateinit var safeContext: Context
    protected var title: String = ""
    protected val handler = Handler(Looper.getMainLooper())
    private val createDisposable = CompositeDisposable()
    private val startDisposable = CompositeDisposable()
    private val resumeDisposable = CompositeDisposable()
    protected val safeActivity: Activity?
    get() {
        val ret = activity
        return if (ret != null && !ret.isFinishing && !ret.isDestroyed) {
            ret
        } else {
            null
        }
    }

    val isActivityAvailable: Boolean
    get() {
        return !(activity == null || activity!!.isFinishing || activity!!.isDestroyed)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = this.javaClass.simpleName
        safeContext = if (activity == null) {
            BaseApplication.instance
        } else {
            (activity as FragmentActivity).applicationContext
        }
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        resumeDisposable.clear()
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        startDisposable.clear()
    }

    protected fun updateActionBar() {
        (activity != null && !(activity as FragmentActivity).isFinishing) || return
        val actionBar = (activity as AppCompatActivity).supportActionBar ?: return
        actionBar.title = title
    }

    override fun onDestroy() {
        super.onDestroy()
        createDisposable.dispose()
        startDisposable.dispose()
        resumeDisposable.dispose()
    }

    override fun onDetach() {
        super.onDetach()
        handler.removeCallbacksAndMessages(null)
    }

    protected fun finishActivity() {
        activity?.finish()
    }

    protected fun bindLifecycle(state: Lifecycle.State, disposable: Disposable) {
        when {
            state.isAtLeast(Lifecycle.State.RESUMED) -> {
                resumeDisposable.add(disposable)
            }
            state.isAtLeast(Lifecycle.State.STARTED) -> {
                startDisposable.add(disposable)
            }
            else -> {
                createDisposable.add(disposable)
            }
        }
    }
}