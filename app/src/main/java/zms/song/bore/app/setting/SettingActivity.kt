package zms.song.bore.app.setting

import android.os.Bundle
import zms.song.bore.app.R
import zms.song.bore.base.BaseActivity

class SettingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_base)
        setActionBar()
        updateTitle("SettingActivity")
        pushFragment(SettingFragment.newInstance())
    }

}
