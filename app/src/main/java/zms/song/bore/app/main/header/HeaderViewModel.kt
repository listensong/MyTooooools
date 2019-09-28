package zms.song.bore.app.main.header

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import zms.song.bore.base.IBaseResponse

/**
 * @author: yisong.liao
 * Time: 19-8-22 上午10:13
 * Desc: zms.song.bore.app.main.header.HeaderViewModel
 */
class HeaderViewModel: ViewModel() {
    val email: ObservableField<String> = ObservableField("")
    val model: ObservableField<String> = ObservableField("")
    val name: ObservableField<String> = ObservableField("")

    private var mHeaderModel: HeaderModel = HeaderModel()

    fun initViewModel(email: String, model: String, name: String): HeaderViewModel {
        this.email.set(email)
        this.model.set(model)
        this.name.set(name)
        return this
    }

    fun loadHeaderInfo() {
        mHeaderModel.getHeader(object : IBaseResponse<HeaderVO> {
            override fun onResponse(t: HeaderVO) {
                setHeaderInfo(t)
            }
        })
    }

    private fun setHeaderInfo(bean: HeaderVO) {
        this.email.set(bean.email)
        this.model.set(bean.model)
        this.name.set(bean.name)
    }
}