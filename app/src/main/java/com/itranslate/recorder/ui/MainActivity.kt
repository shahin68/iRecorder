package com.itranslate.recorder.ui

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.itranslate.recorder.R
import com.itranslate.recorder.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addNavControllerDestinationListener()

    }

    /**
     * Register listener for navigation controller
     */
    private fun addNavControllerDestinationListener() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_navigation_host) as NavHostFragment
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> setActionbarTitle(getString(R.string.app_name))
                R.id.recordingsFragment -> setActionbarTitle(getString(R.string.actionbar_title_recordings))
            }
        }
    }

    /**
     * set action bar title
     */
    private fun setActionbarTitle(string: String) {
        supportActionBar?.title = string
    }

}