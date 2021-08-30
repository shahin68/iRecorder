package com.itranslate.recorder.ui.fragments.recordings

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.pressBack
import androidx.test.filters.MediumTest
import com.itranslate.recorder.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.robolectric.annotation.Config

@MediumTest
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
class RecordingsFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

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