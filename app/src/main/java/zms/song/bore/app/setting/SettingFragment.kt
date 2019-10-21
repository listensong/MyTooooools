package zms.song.bore.app.setting


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import zms.song.bore.app.R

import zms.song.bore.base.BaseFragment


class SettingFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        title = "Hello Setting"
        updateActionBar()
        return view

    }


    companion object {
        @JvmStatic
        fun newInstance() = SettingFragment()
    }
}
