package mobi.largemind.payoneer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import mobi.largemind.payoneer.data.DataState
import mobi.largemind.payoneer.data.PaymentsRepository
import okio.IOException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class PaymentsRepositoryTest {
    private lateinit var service: MockPaymentsService
    private lateinit var repository: PaymentsRepository

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        service = MockPaymentsService()
        repository = PaymentsRepository(service)
    }

    @Test
    fun `network failure should return LOADING then ERROR`() {
        service.returnWithThrowable(TimeoutException())

        val liveData = repository.fetchPaymentMethods()

        val states = liveData.getOrAwaitMultipleValues(dataCount = 2) {}
        assert(states.size == 2)
        assert(states[0]?.state == DataState.State.LOADING)
        assert(states[1]?.state == DataState.State.ERROR)
        assert(states[1]?.throwable is TimeoutException)
    }

    @Test
    fun `failed HTTP requests should return LOADING then ERROR`() {
        service.returnWithHtttpErrorCode(401)

        val liveData = repository.fetchPaymentMethods()

        val states = liveData.getOrAwaitMultipleValues(dataCount = 2) {}
        assert(states.size == 2)
        assert(states[0]?.state == DataState.State.LOADING)
        assert(states[1]?.state == DataState.State.ERROR)
        assert(states[1]?.throwable is IOException)
    }

    @Test
    fun `200 OK response should return LOADING then DATA`() {
        val response = buildResponse()
        service.returnWithResponse(response)

        val liveData = repository.fetchPaymentMethods()

        val states = liveData.getOrAwaitMultipleValues(dataCount = 2) {}
        assert(states.size == 2)
        assert(states[0]?.state == DataState.State.LOADING)
        assert(states[1]?.state == DataState.State.DATA)
        assert(states[1]?.data == response)
    }
}

fun <T> LiveData<T>.getOrAwaitMultipleValues(
    time: Long = 2,
    dataCount: Int = 1,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    afterObserve: () -> Unit = {}
): List<T?> {

    val data = mutableListOf<T?>()
    val latch = CountDownLatch(dataCount)

    val observer = Observer<T> { o ->
        data.add(o)
        latch.countDown()
    }
    this.observeForever(observer)

    afterObserve.invoke()

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        this.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data.toList()
}