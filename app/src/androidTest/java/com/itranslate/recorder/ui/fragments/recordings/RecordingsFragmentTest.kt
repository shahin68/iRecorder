package com.itranslate.recorder.ui.fragments.recordings

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.itranslate.recorder.R
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@MediumTest
@RunWith(AndroidJUnit4::class)
class RecordingsFragmentTest {

    /**
     * Test navigation ui to verify whether the nav controller pops back to previous
     * fragment (which is home fragment) or not
     */
    @Test
    fun pressBackButton_popBackStack() {
        val navController = Mockito.mock(NavController::class.java)

        val scenario = launchFragmentInContainer<RecordingsFragment>(themeResId = R.style.Theme_IRecorder)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        pressBack()

        Mockito.verify(navController).popBackStack()
    }
}