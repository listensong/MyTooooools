package zms.song.bore.app.main.header

import zms.song.bore.base.IBaseResponse
import zms.song.bore.extend.DeviceUtil

/**
 * @author song
 * Time: 19-8-22 上午10:20
 * Desc: zms.song.bore.app.main.header.HeaderVO
 */
class HeaderModel {
    private object Holder {
        val INSTANCE = HeaderModel()
    }

    companion object {
        fun get(): HeaderModel = Holder.INSTANCE
    }

    fun getHeaderInfo() = HeaderVO("Power by HelloWorld@IT.com",
            "型号：" + DeviceUtil.getModelName(),
            DeviceUtil.getAppName())

    fun getHeader(response: IBaseResponse<HeaderVO>) {
        response.onResponse(
                HeaderVO("Power by HelloWorld@IT.com", "型号：" + DeviceUtil.getModelName(), DeviceUtil.getAppName())
        )
    }
}
data class HeaderVO(var email: String, var model: String, var name: String)