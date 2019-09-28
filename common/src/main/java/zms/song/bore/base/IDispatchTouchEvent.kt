package zms.song.bore.base

import android.view.MotionEvent

/**
 * @author: yisong.liao
 * Time: 19-8-22 上午11:22
 * Desc: zms.song.bore.base.IDispatchTouchEvent
 */
interface IDispatchTouchEvent {
    fun onDispatchTouchEvent(event: MotionEvent)
}
