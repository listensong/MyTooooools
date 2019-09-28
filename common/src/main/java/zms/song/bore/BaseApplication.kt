package zms.song.bore

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.ProcessLifecycleOwner
import com.bennyhuo.kotlin.coroutines.android.mainscope.MainScope
import zms.song.bore.base.ApplicationLifecycleObserver
import kotlin.properties.Delegates

/**
 * @author: yisong.liao
 * Time: 19-8-22 上午10:27
 * Desc: zms.song.bore.BaseApplication
 */
open class BaseApplication: Application() {
    companion object {
        private val TAG = BaseApplication::class.java.simpleName

        var instance: BaseApplication by Delegates.notNull()
            private set
    }

    private val mHandler = Handler(Looper.getMainLooper())
    private val mApplicationLifecycleObserver = ApplicationLifecycleObserver()

    private val mActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            if (activity != null) {
                Log.d(TAG, activity.javaClass.simpleName + " onCreated")
            }
        }

        override fun onActivityStarted(activity: Activity) {

        }

        override fun onActivityResumed(activity: Activity) {

        }

        override fun onActivityPaused(activity: Activity) {

        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

        }

        override fun onActivityDestroyed(activity: Activity?) {
            if (activity != null) {
                Log.d(TAG, activity.javaClass.simpleName + " onDestroyed")
            }
        }
    }

//    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(base)
//        //MultiDex.install(this)
//    }

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        MainScope.setUp(this)
        instance = this
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        ProcessLifecycleOwner.get().lifecycle.addObserver(mApplicationLifecycleObserver)
    }

    @CallSuper
    override fun onTerminate() {
        mHandler.removeCallbacksAndMessages(null)
        unregisterActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        ProcessLifecycleOwner.get().lifecycle.removeObserver(mApplicationLifecycleObserver)
        super.onTerminate()
    }
}