package zms.song.bore.base

/**
 * @author: yisong.liao
 * Time: 19-8-22 上午10:31
 * Desc: zms.song.bore.base.IBaseResponse
 */
interface IBaseResponse<in T> {
    fun onResponse(t: T)
}