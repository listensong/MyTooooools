package zms.song.bore.common.network.retrofit

import android.util.Log
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import zms.song.bore.BuildConfig

/**
 * @author song
 */
class InterceptorModifyRequest(
        private vararg val modifyRequests: (original: Request, builder: Request.Builder) -> Boolean
) : Interceptor {
    override fun intercept(originalChain: Interceptor.Chain): Response {
        return originalChain.let {chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder().apply {
                header(ApiCallConstant.CONTENT_TYPE   , ApiCallConstant.CONTENT_TYPE_APPLICATION_JSON)
                header(ApiCallConstant.ACCEPT_LANGUAGE, ApiCallConstant.AC_LAN_ZH_CN)
                header(ApiCallConstant.CACHE_CONTROL  , ApiCallConstant.CACHE_CONTROL_NO_CACHE)

                header(ApiCallConstant.AUTHORIZATION, getAuthorization())
            }

            modifyRequests.forEach {
                it(originalRequest, requestBuilder)
            }

            requestBuilder.build().run {
                listHeaders(this.headers)
                chain.proceed(this)
            }
        }
    }

    private fun getAuthorization(): String {

        return ""
    }

    private fun listHeaders(headers: Headers?) {
        if (!BuildConfig.DEBUG) {
            return
        }
        if (headers == null) {
            Log.e("HelloWorld", "listHeaders == null")
        } else {
            Log.e("HelloWorld", "listHeaders \n$headers")

        }
    }
}