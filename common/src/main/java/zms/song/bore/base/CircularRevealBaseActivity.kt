package zms.song.bore.base

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.CallSuper
import zms.song.bore.TOUCH_X
import zms.song.bore.TOUCH_Y
import zms.song.bore.extend.getAndroidAttrPX
import zms.song.bore.extend.getStatusBarHeight


/**
 * @author song
 * Created by song on 2018/3/12.
 */
abstract class CircularRevealBaseActivity :
        BaseActivity(),
        ICircularReveal by CircularRevealBlock() {

    protected var mAppBar: View? = null
    protected var mActionBarHeight: Int = 0
    protected var mActionLayoutHeight: Int = 0
    protected var mStatusBarHeight: Int = 0

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCircularRevealActivity(this)

        intent?.extras?.let {
            setTouchValue(it.getInt(TOUCH_X, 0), it.getInt(TOUCH_Y, 0))
        }

        mActionBarHeight = getAndroidAttrPX(android.R.attr.actionBarSize)
        mStatusBarHeight = getStatusBarHeight()
        mActionLayoutHeight = mActionBarHeight + mStatusBarHeight
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        onDispatchTouchEvent(ev, supportFragmentManager)
        return super.dispatchTouchEvent(ev)
    }
}
