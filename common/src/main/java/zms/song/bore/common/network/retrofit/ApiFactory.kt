package zms.song.bore.common.network.retrofit

import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import zms.song.bore.BaseApplication
import java.util.concurrent.TimeUnit

/**
 * @author song
 * And, How To Use?
 * For Example:
 * ## - 1
 *  data class GitModel (....)
 *
 * ## - 2
 *  interface GitApi {
        @GET("users/{user}")
        abstract fun getFeed(@Path("user") user: String): LifecycleCall<GitModel>
    }
 *
 * ## - 3
 *  GlobalScope.launch {
        val str : GitModel = ApiFactory
                .getInstance()
                .createApi(GitApi::class.java, "https://api.github.com/")
                .getFeed(<GitUserId>)
                .await()
                Log.e("Result", "str ->: $str")
       //handle result here
    }
    Or:
    requestAsync (
        action = {
            ApiFactory
                .getInstance()
                .createApi(GitApi::class.java, "https://api.github.com/")
                .getFeed(<GitUserId>)
                .await()
        },
        result = { gitModel ->
            //handle gitModel here
        }
    )
 *
 */
class ApiFactory {
    private val okHttpClient by lazy {
        OkHttpClient
                .Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .cache(Cache(BaseApplication.instance.cacheDir, 52428800))//50*1024*1024 = 52428800
                //.addInterceptor(ChuckInterceptor(BaseApplication.instance))
                .build()
    }

    fun <T> createApi(clz: Class<T>,
                      endPoint: String,
                      interceptorModifyRequest: InterceptorModifyRequest = InterceptorModifyRequest()) : T {
        val retrofit = Retrofit
                .Builder()
                .client(okHttpClient.also {
                    it.newBuilder().addInterceptor(interceptorModifyRequest)
                })
                .baseUrl(endPoint)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LifecycleCallAdapterFactory(BaseApplication.instance))
                .build()
        return retrofit.create(clz)
    }

    companion object {
        fun getInstance(): ApiFactory = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = ApiFactory()
    }
}