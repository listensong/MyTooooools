package zms.song.bore.common.network.retrofit


import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author song
 */
class LifecycleCallAdapterFactory(
        private val context: Context,
        private val enableCancel: Boolean = true
) : CallAdapter.Factory() {

    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *>? {
        if (returnType == null || getRawType(returnType) != ILifecycleCall::class.java) {
            return null
        }

        if (returnType !is ParameterizedType) {
            throw IllegalStateException("Call must be have generic type")
        }

        val responseType = getParameterUpperBound(0, returnType)
        if (context is LifecycleOwner) {
            if (context.lifecycle is LifecycleRegistry) {
                return ErrorHandleCallAdapter<Any>(
                        responseType, context.lifecycle as LifecycleRegistry, enableCancel
                )
            }
        }

        return ErrorHandleCallAdapter<Any>(responseType, enableCancel = enableCancel)
    }
}