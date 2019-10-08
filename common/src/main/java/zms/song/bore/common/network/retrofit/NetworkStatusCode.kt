package zms.song.bore.common.network.retrofit

/**
 * @author song
 */
object NetworkStatusCode {
    const val ERROR_SOCKET_EXCEPTION = -101
    const val ERROR_CONNECT = -102
    const val ERROR_SSL_HANDSHAKE = -103
    const val ERROR_HTTP = -104
    const val ERROR_UNKNOWN_HOST = -105
    const val ERROR_EMPTY_BODY = -106
    const val ERROR_SERVER_IO = -107
    const val ERROR_UNEXPECTED = -176

    const val ERROR_NETWORK = 504
    const val ERROR_NOT_FOUND = 404
}