package zms.song.bore.base

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.annotation.AttrRes
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import zms.song.bore.R

/**
 * @author: yisong.liao
 * Time: 19-8-21 下午9:14
 * Desc: zms.song.bore.app.base.BaseActivity
 */
abstract class BaseActivity : AppCompatActivity() {
    protected lateinit var TAG : String
    protected var mToolbar: Toolbar? = null
    protected val handler = Handler(Looper.getMainLooper())

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TAG = this.javaClass.simpleName
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }

    protected fun setActionBar() {
        mToolbar = findViewById(R.id.toolbar)

        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
        }

        supportActionBar?.run {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.btn_back)
            setHomeActionContentDescription(resources.getString(R.string.action_bar_back_button_navi_up))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected fun postDelayed(delay: Long = 1000, action: () -> Unit) {
        handler.postDelayed({action()}, delay)
    }
}