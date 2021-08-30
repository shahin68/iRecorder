package com.itranslate.recorder.ui.fragments.home

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.itranslate.recorder.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.annotation.Config

@MediumTest
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
class HomeFragmentTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    /**
     * Testing navigation ui to verify whether by clicking on show recording button
     * the nav controller navigates to recordings fragment or not
     */
    @Test
    fun clickOnShowRecordingsButton_navigateToRecordingsFragment() {
        val navController = mock(NavController::class.java)

        val scenario = launchFragmentInContainer<HomeFragment>(themeResId = R.style.Theme_IRecorder)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(withId(R.id.btn_home_show_recordings)).perform(click())

        verify(navController).navigate(
            HomeFragmentDirections.actionHomeFragmentToRecordingsFragment()
        )
    }

}