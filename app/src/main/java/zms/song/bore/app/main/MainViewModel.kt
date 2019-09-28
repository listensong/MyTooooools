package zms.song.bore.app.main

import androidx.lifecycle.ViewModel
import zms.song.bore.base.CoverObservableArrayList
import zms.song.bore.base.SingleLiveEvent

/**
 * @author: yisong.liao
 * Time: 19-8-22 上午10:13
 * Desc: zms.song.bore.app.main.MainViewModel
 */
class MainViewModel: ViewModel() {
    private val mLiveEvent: SingleLiveEvent<Int> = SingleLiveEvent()
    //val summaries: CoverObservableArrayList<SummaryEntity> = CoverObservableArrayList()

    init{
//        mObservableSummary.value = null
//        mObservableSummary.addSource(TipsInfoRepository.get().observableSummary, { entities ->
//            mObservableSummary.value = entities
//            if (entities != null) {
//                summaries.update(entities)
//            }
//        })
    }

    fun onClick(eventId: Int) {
        mLiveEvent.value = eventId
    }
}