package com.ig.android.core.app.mock

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.rules.ExternalResource
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

    fun setMockingDispatcherFor(responseMap: Pair<String, MockResponse>) {
        mockWebServer.dispatcher = MockDispatcher(responseMap)
    }
}


private class MockDispatcher(private val responseMap: Pair<String, MockResponse>) : Dispatcher() {
    @Throws(InterruptedException::class)
    override fun dispatch(request: RecordedRequest): MockResponse {
        when (request.path) {
            responseMap.first -> return responseMap.second
        }
        return MockResponse().setResponseCode(404)
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
