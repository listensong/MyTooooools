package zms.song.bore.common.network.retrofit

import android.content.Context
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import zms.song.bore.BaseApplication
import zms.song.bore.BuildConfig
import java.net.Proxy
import java.util.concurrent.TimeUnit

/**
 * @author song
 */
class ApiCallImpl {

    private var clientCacheDir: String  = ""
    private val vocBaseUrl = "https://www.baidu.com/"

    private val okHttpClient by lazy {
        OkHttpClient
                .Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .proxy(Proxy.NO_PROXY)
                .cache(Cache(BaseApplication.instance.cacheDir, 52428800))//50*1024*1024 = 52428800
                .addInterceptor(ChuckInterceptor(BaseApplication.instance))
                .addInterceptor(HttpLoggingInterceptor()
                        .setLevel(
                                if (BuildConfig.DEBUG) { HttpLoggingInterceptor.Level.BODY }
                                else { HttpLoggingInterceptor.Level.BASIC } ))
                .build()
    }

    fun <T> callOsBetaApi(context: Context,
                          clazz: Class<T>,
                          interceptorModifyRequest: InterceptorModifyRequest = InterceptorModifyRequest()
    ) : T {
        return Retrofit
                .Builder()
                .client(okHttpClient.newBuilder().addInterceptor(interceptorModifyRequest).build())
                .baseUrl(vocBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LifecycleCallAdapterFactory(context))
                .build()
                .create(clazz)
    }

    companion object {
        @JvmStatic
        fun getInstance(): ApiCallImpl = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = ApiCallImpl()
    }
}