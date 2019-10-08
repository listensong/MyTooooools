package zms.song.bore.app

import android.content.Intent
import android.os.Bundle
import zms.song.bore.TOUCH_X
import zms.song.bore.TOUCH_Y
import zms.song.bore.app.main.WelcomeActivity
import zms.song.bore.base.BaseActivity
import zms.song.bore.extend.getWindowMetrics
import zms.song.bore.extend.setFullScreen

/**
 * @author song
 */
class LauncherActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setFullScreen()
        setContentView(R.layout.activity_launcher)

        val bundle = Bundle()
        /*
        主要是通过 getSourceBounds() 来获取app图标在launcher上的位置区域rect
        在P OS上拿不到sourceBounds
        */
        val rect = intent.sourceBounds
        if (rect == null) {
            /*
            非触摸形式启动app的话，rect是为null的。
            比如从别的app跳转启动的话，rect就是null的。
            如果rect为空，就让app从屏幕中心位置开始揭露效果动画。
            */
            val dm = getWindowMetrics()
            bundle.putInt(TOUCH_X, dm.widthPixels / 2)
            bundle.putInt(TOUCH_Y, dm.heightPixels / 2)
        } else {
            bundle.putInt(TOUCH_X, rect.centerX())
            bundle.putInt(TOUCH_Y, rect.centerY())
        }

        val mainIntent = Intent(this, WelcomeActivity::class.java)
        mainIntent.putExtras(bundle)
        postDelayed(100) {
            startActivity(mainIntent)
            finish()
        }
    }
}
