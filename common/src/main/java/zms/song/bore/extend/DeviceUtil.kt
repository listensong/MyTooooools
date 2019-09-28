package care.android.com.caretips.extend

import android.content.Context
import android.os.Build
import zms.song.bore.BaseApplication
import zms.song.bore.R

/**
 *
 * Created by song on 2018/2/23.
 */
object DeviceUtil {
    fun getModelName(): String = Build.MODEL

    fun getAppName(): String = BaseApplication.instance.getString(R.string.app_name)

}