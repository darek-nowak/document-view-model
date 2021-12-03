package com.ig.android.core.app.mock

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.rules.ExternalResource
import timber.log.Timber
import java.io.InputStreamReader

private const val SERVER_PORT = 8080

class MockWebServerRule : ExternalResource() {

    private val mockWebServer = MockWebServer()

    override fun before() {
        super.before()
        mockWebServer.start(SERVER_PORT)
    }

    override fun after() {
        super.after()
        mockWebServer.shutdown()
    }

    fun setMockingDispatcherFor(vararg responses: Pair<String, MockResponse>) {
        mockWebServer.dispatcher = MockDispatcher(mapOf(*responses))
    }
}


private class MockDispatcher(private val responses: Map<String, MockResponse>) : Dispatcher() {
    @Throws(InterruptedException::class)
    override fun dispatch(request: RecordedRequest): MockResponse {
        Timber.d("darek ${request.path}")
        return responses[request.path] ?: MockResponse().setResponseCode(404)
    }
}

fun MockResponse.withBody(fileName: String) =
    setBody(fileName.extractResponseFromAssets())
        .setResponseCode(200)

private fun String.extractResponseFromAssets() = InstrumentationRegistry
    .getInstrumentation().context.assets.open("responses/$this").run {
        val inputStreamReader = InputStreamReader(this)
        val text = inputStreamReader.readText()
        inputStreamReader.close()
        text
    }
