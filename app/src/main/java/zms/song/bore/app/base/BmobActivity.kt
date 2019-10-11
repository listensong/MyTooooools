package zms.song.bore.app.base

import android.os.Bundle
import zms.song.bore.app.R
import zms.song.bore.base.BaseActivity

class BmobActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmob)
        setActionBar()
        updateTitle("HelloWorld Bmob")
    }
}
