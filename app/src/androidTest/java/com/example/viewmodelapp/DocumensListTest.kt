package com.example.viewmodelapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.viewmodelapp.presentation.MainActivity
import com.ig.android.core.app.mock.MockWebServerRule
import com.ig.android.core.app.mock.withBody
import okhttp3.mockwebserver.MockResponse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DocumentsListTest {

    @JvmField
    @Rule
    val server = MockWebServerRule()

    @get:Rule
    val activityRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(
        MainActivity::class.java
    )

    @Test
    fun first_mockwebserver_test() {
        server.setMockingDispatcherFor(DOCUMENTS_LIST_RESPONSE)

        onView(withText("Abra Kadabra"))
            .check(matches(isDisplayed()))
    }

    private companion object {
        val DOCUMENTS_LIST_RESPONSE =
            "/repos/darek-nowak/json/contents" to MockResponse().withBody("documents_list.json")
    }
}