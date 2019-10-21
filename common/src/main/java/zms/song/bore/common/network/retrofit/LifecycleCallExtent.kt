package zms.song.bore.common.network.retrofit

import android.text.TextUtils
import android.util.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.ResponseBody
import org.codehaus.jackson.map.ObjectMapper
import org.codehaus.jackson.type.TypeReference
import retrofit2.HttpException
import retrofit2.Response
import ru.gildor.coroutines.retrofit.Result
import ru.gildor.coroutines.retrofit.getOrNull
import zms.song.bore.common.network.retrofit.NetworkStatusCode.ERROR_CONNECT
import zms.song.bore.common.network.retrofit.NetworkStatusCode.ERROR_EMPTY_BODY
import zms.song.bore.common.network.retrofit.NetworkStatusCode.ERROR_SERVER_IO
import zms.song.bore.common.network.retrofit.NetworkStatusCode.ERROR_SOCKET_EXCEPTION
import zms.song.bore.common.network.retrofit.NetworkStatusCode.ERROR_SSL_HANDSHAKE
import zms.song.bore.common.network.retrofit.NetworkStatusCode.ERROR_UNEXPECTED
import zms.song.bore.common.network.retrofit.NetworkStatusCode.ERROR_UNKNOWN_HOST
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.UnknownHostException
import java.util.*
import javax.net.ssl.SSLHandshakeException
import kotlin.coroutines.resume

/**
 * @author song
 */
class ResponseInfo @Throws(IOException::class) constructor(response: String?) {
    val parameterMapList: MutableList<Map<String, Any>> = ArrayList()

    init {
        var resultResponse = response
        val parameterMap: Map<String, Any>?
        if (resultResponse != null && resultResponse != "") {
            resultResponse = "{\"map\":$resultResponse}"
            parameterMap = ObjectMapper().readValue<Map<String, Any>>(resultResponse, object : TypeReference<Map<String, Any>>() {})

            if (parameterMap != null && parameterMap["map"] != null) {
                if (parameterMap["map"] is ArrayList<*>) {
                    parameterMapList.addAll(parameterMap["map"] as List<Map<String, Any>>)
                } else {
                    parameterMapList.add(parameterMap["map"] as Map<String, Any>)
                }
            } else {
                Log.e("LifecycleCallExt", "parameterMap is null!!")
            }
        }
    }
}


fun Result<ResponseBody>.handle(
        success:(statusCode: Int, info: ResponseInfo) -> Unit ,
        fail:(statusCode: Int, error: NetworkError) -> Unit
) {
    val body = getOrNull()
    if (body == null) {
        when (this) {
            is Result.Error -> when (val result = exception) {
                is HttpException -> {
                    handleVocHttpException(result, fail)
                }
                else -> {
                    fail(result.code(), NetworkError(response.code, "unknown exception"))
                }
            }
            is Result.Exception -> when (val result = exception) {
                is IOException -> {
                    fail(ERROR_SERVER_IO, NetworkError(ERROR_SERVER_IO, result.message))
                }
                is SocketException -> {
                    fail(ERROR_SOCKET_EXCEPTION, NetworkError(ERROR_SOCKET_EXCEPTION, result.message))
                }
                is ConnectException -> {
                    fail(ERROR_CONNECT, NetworkError(ERROR_CONNECT, result.message))
                }
                is SSLHandshakeException -> {
                    fail(ERROR_SSL_HANDSHAKE, NetworkError(ERROR_SSL_HANDSHAKE, result.message))
                }
                is HttpException -> {
                    handleVocHttpException(result, fail)
                }
                is UnknownHostException -> {
                    fail(ERROR_UNKNOWN_HOST, NetworkError(ERROR_UNKNOWN_HOST, result.message))
                }
                is NullPointerException -> {
                    fail(ERROR_EMPTY_BODY, NetworkError(ERROR_EMPTY_BODY, result.message))
                }
                else -> {
                    fail(ERROR_UNEXPECTED, NetworkError(ERROR_UNEXPECTED, result.message))
                }
            }
            else -> fail(ERROR_EMPTY_BODY, NetworkError(ERROR_EMPTY_BODY, "Empty Body"))
        }
    } else {
        if (this is Result.Ok) {
            when(val responseCode = response.code) {
                in 200..399 -> {
                    try {
                        success(responseCode, ResponseInfo(body.string()))
                    } catch (e: IOException) {
                        Log.e("HS", e.message, e)
                        fail(ERROR_SERVER_IO, NetworkError(ERROR_SERVER_IO, "server response IOException"))
                    }
                }
                401 -> { fail(responseCode, NetworkError(responseCode, "Unauthenticated error"))}
                in 400..499 -> { fail(responseCode, NetworkError(responseCode, "Client error")) }
                in 500..599 -> { fail(responseCode, NetworkError(responseCode, "Server error")) }
                else -> { fail(responseCode, NetworkError(responseCode, "Unexpected error")) }
            }
        }
    }
}

private fun handleVocHttpException(
        exp: HttpException,
        fail:(statusCode: Int, error: NetworkError) -> Unit
) {
    try {
        var errorCode = 0
        var errorMessage = "unknown error"
        val srcString = exp.response().errorBody()?.string()
        if (!TextUtils.isEmpty(srcString)) {
            ObjectMapper().readValue<Map<String, Any>>(
                    srcString,
                    object : TypeReference<Map<String, Any>>() {}
            )?.let { map ->
                if (map.containsKey("errorCode")) {
                    errorCode = Integer.parseInt(map["errorCode"] as String)
                }
                if (map.containsKey("errorMessage")) {
                    errorMessage = map["errorMessage"] as String
                }
            }
        }
        fail(exp.code(), NetworkError(errorCode, errorMessage))
    } catch (e: Exception) {
        fail(ERROR_SERVER_IO, NetworkError(ERROR_SERVER_IO, "Server Response IOException"))
    }
}

suspend fun <T : Any> ILifecycleCall<T>.awaitResult(): Result<T> {
    return suspendCancellableCoroutine { cancellableContinuation ->
        cancellableContinuation.invokeOnCancellation {
            if (cancellableContinuation.isCancelled) {
                cancel()
            }
        }

        enqueue(object : ILifecycleCallback<T> {
            override fun onFinish() {}

            override fun onSuccess(statusCode: Int, response: Response<T>) {
                cancellableContinuation.resumeWith(runCatching {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body == null) {
                            Result.Exception(NullPointerException("Response body is null"))
                        } else {
                            Result.Ok(body, response.raw())
                        }
                    } else {
                        Result.Error(HttpException(response), response.raw())
                    }
                })
            }

            override fun onFail(statusCode: Int, throwable: Throwable) {
                if (cancellableContinuation.isCancelled) {
                    return
                }
                cancellableContinuation.resume(Result.Exception(throwable))
            }
        })
    }
}