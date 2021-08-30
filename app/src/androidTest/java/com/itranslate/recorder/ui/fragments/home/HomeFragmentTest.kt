package com.itranslate.recorder.ui.fragments.home

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.itranslate.recorder.R
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

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