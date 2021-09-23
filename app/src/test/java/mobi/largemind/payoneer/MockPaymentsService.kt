package mobi.largemind.payoneer

import kotlinx.coroutines.*
import mobi.largemind.payoneer.data.PaymentsResponse
import mobi.largemind.payoneer.data.PaymentsService
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class MockPaymentsService : PaymentsService {

    private var throwable: Throwable? = null
    private var responseCode: Int = 200
    private var response: PaymentsResponse? = null

    override fun fetchPayments(): Call<PaymentsResponse> {
        return callResponse
    }

    private val callResponse = object : Call<PaymentsResponse> {
        override fun clone(): Call<PaymentsResponse> {
            return this
        }

        override fun execute(): Response<PaymentsResponse> {
            TODO("Not yet implemented")
        }

        override fun enqueue(callback: Callback<PaymentsResponse>) {
            val call = this
            GlobalScope.launch(Dispatchers.IO) {
                delay(Random.nextLong(100, 600))
                when {
                    throwable != null -> {
                        callback.onFailure(call, throwable!!)
                    }
                    response != null -> {
                        callback.onResponse(call, Response.success(response))
                    }
                    else -> {
                        callback.onResponse(call, Response.error(responseCode, "empty".toResponseBody()))
                    }
                }
            }
        }

        override fun isExecuted(): Boolean {
            return false
        }

        override fun cancel() {
            TODO("Not yet implemented")
        }

        override fun isCanceled() = false

        override fun request(): Request {
            TODO("Not yet implemented")
        }

        override fun timeout(): Timeout {
            TODO("Not yet implemented")
        }
    }

    fun returnWithThrowable(throwable: Throwable) {
        this.throwable = throwable
    }

    fun returnWithHtttpErrorCode(code: Int) {
        throwable = null
        responseCode = code
    }

    fun returnWithResponse(response: PaymentsResponse) {
        this.response = response
        responseCode = 200
        throwable = null
    }
}

fun buildResponse(): PaymentsResponse {
    val link = PaymentsResponse.Links()
    link.logo = "http://localhost/logo.png"
    val network = PaymentsResponse.Network()
    network.code = "FAKE"
    network.label = "Fake Method"
    network.links = link
    val networks = PaymentsResponse.Networks()
    networks.networks = listOf(network)
    val response = PaymentsResponse()
    response.networks = networks
    return response
}