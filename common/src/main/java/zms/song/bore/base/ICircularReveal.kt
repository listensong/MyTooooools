package zms.song.bore.base

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import androidx.fragment.app.FragmentManager
import zms.song.bore.R
import zms.song.bore.TOUCH_X
import zms.song.bore.TOUCH_Y
import zms.song.bore.extend.OnCircularRevealListener
import zms.song.bore.extend.applyCircularReveal
import zms.song.bore.util.UiUtil

/**
 * @author: yisong.liao
 * Time: 19-8-22 上午9:53
 * Desc: zms.song.bore.base.ICircularReveal
 */
interface ICircularReveal {

    var circularRevealX: Int
    var circularRevealY: Int

    fun setCircularRevealActivity(attachActivity: Activity) {

    }

    fun onDispatchTouchEvent(ev: MotionEvent, fManager: FragmentManager) {

    }

    fun setTouchValue(x: Int, y: Int) {

    }

    fun setPreDrawView(view: View?) {

    }

    fun removePreDrawView(view: View?) {

    }

    fun runCircularRevealAnim(view: View?, show: Boolean = true, x: Int = circularRevealX, y: Int = circularRevealY) {

    }

    fun startActivityWithCircularReveal(key: Int, bundle: Bundle = Bundle()) {

    }

    fun setCircularRevealShowEndAction(callback: () -> Unit) {

    }

    fun setCircularRevealHideEndAction(callback: () -> Unit) {

    }

    fun onCircularRevealStart(callback: () -> Unit) {

    }

    fun setPreDrawAction(callback: () -> Unit) {

    }
}

class CircularRevealBlock : ICircularReveal, ViewTreeObserver.OnPreDrawListener {

    private var mTouchX = 0f
    private var mTouchY = 0f
    private var mInitX = 0
    private var mInitY = 0
    private var mIsInAnim = false

    private lateinit var mAttachActivity: Activity

    private var mPreDrawCallback: (() -> Unit)? = null
    private var mCircularRevealShowCallback: (() -> Unit)? = null
    private var mCircularRevealHideCallback: (() -> Unit)? = null
    private var mCircularRevealStartCallback: (() -> Unit)? = null

    override var circularRevealX: Int
        get() = mInitX
        set(value) {
            this.mTouchX = value.toFloat()
        }

    override var circularRevealY: Int
        get() = mInitY
        set(value) {
            this.mTouchY = value.toFloat()
        }

    override fun setCircularRevealActivity(attachActivity: Activity) {
        mAttachActivity = attachActivity
    }

    override fun onDispatchTouchEvent(ev: MotionEvent, fManager: FragmentManager) {
        if (ev.action == MotionEvent.ACTION_UP) {
            mTouchX = ev.x
            mTouchY = ev.y
            val fragment = fManager.findFragmentById(R.id.container)
            if (fragment is IDispatchTouchEvent) {
                (fragment as IDispatchTouchEvent).onDispatchTouchEvent(ev)
            }
        }
    }

    override fun setTouchValue(x: Int, y: Int) {
        mInitX = x
        mInitY = y
        mTouchX = x.toFloat()
        mTouchY = y.toFloat()
    }

    override fun setPreDrawView(view: View?) {
        view?.viewTreeObserver?.addOnPreDrawListener(this)
    }

    override fun removePreDrawView(view: View?) {
        view?.viewTreeObserver?.removeOnPreDrawListener(this)
    }

    override fun runCircularRevealAnim(view: View?, show: Boolean, x: Int, y: Int) {
        view ?: return

        if (mAttachActivity.isFinishing || mAttachActivity.isDestroyed) {
            return
        }

        if (mIsInAnim) {
            return
        }
        mIsInAnim = true
        mCircularRevealStartCallback?.invoke()
        view.applyCircularReveal(x, y, show, object : OnCircularRevealListener {
            override fun onShowEnd() {
                mIsInAnim = false
                mCircularRevealShowCallback?.invoke()
            }

            override fun onHideEnd() {
                mIsInAnim = false
                mCircularRevealHideCallback?.invoke()
            }
        })
    }

    override fun startActivityWithCircularReveal(key: Int, bundle: Bundle) {
        bundle.putInt(TOUCH_X, mTouchX.toInt())
        bundle.putInt(TOUCH_Y, mTouchY.toInt())
        //Router.instance().transfer(mAttachActivity, key, bundle)
    }

    override fun setPreDrawAction(callback: () -> Unit) {
        mPreDrawCallback = callback
    }

    override fun onPreDraw(): Boolean {
        Log.e("HelloWorld", "onPreDraw")
        mPreDrawCallback?.invoke()
        return true
    }

    override fun setCircularRevealShowEndAction(callback: () -> Unit) {
        mCircularRevealShowCallback = callback
    }

    override fun setCircularRevealHideEndAction(callback: () -> Unit) {
        mCircularRevealHideCallback = callback
    }

    override fun onCircularRevealStart(callback: () -> Unit) {
        mCircularRevealStartCallback = callback
    }
}