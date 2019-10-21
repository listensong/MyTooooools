package zms.song.bore.common.base

/**
 * @author: Listensong
 * Time: 19-10-8 下午4:43
 * Desc: zms.song.bore.common.base.Mapper
 * etc:
 * DTO -> VO
 * VO  -> DTO
 */
interface Mapper <out T> {
    fun transform(): T
}