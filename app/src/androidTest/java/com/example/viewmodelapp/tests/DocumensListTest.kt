package com.example.viewmodelapp.tests

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.viewmodelapp.R
import com.example.viewmodelapp.presentation.MainActivity
import com.ig.android.core.app.mock.MockWebServerRule
import com.ig.android.core.app.mock.withBody
import okhttp3.mockwebserver.MockResponse
import org.hamcrest.core.IsNot.not
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
    fun documentListSuccess() {
        server.setMockingDispatcherFor(DOCUMENTS_LIST_RESPONSE_SUCCESS)

        onView(withText("Mark Twain"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun documentsListError() {
        server.setMockingDispatcherFor(DOCUMENTS_LIST_RESPONSE_ERROR)

        onView(withText("Error fetching data"))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(ViewMatchers.withId(R.id.progressBar))
            .check(matches(not(isDisplayed())))
    }

    private companion object {
        val DOCUMENTS_LIST_RESPONSE_SUCCESS =
            "/repos/darek-nowak/json/contents" to MockResponse().withBody("documents_list.json")
        val DOCUMENTS_LIST_RESPONSE_ERROR =
            "/repos/darek-nowak/json/contents" to MockResponse().setResponseCode(401)
    }
}