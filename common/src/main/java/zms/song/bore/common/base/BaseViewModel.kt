package zms.song.bore.common.base

import android.util.Log
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel

/**
 * @author: Listensong
 * Time: 19-10-8 下午3:58
 * Desc: zms.song.bore.common.base.BaseViewModel
 */
abstract class BaseViewModel : ViewModel() {

    @CallSuper
    override fun onCleared() {
        Log.i("BaseViewModel", "onCleared")
        super.onCleared()
    }
}