package com.itranslate.recorder.general.extensions

import androidx.fragment.app.Fragment
import com.google.android.material.transition.platform.MaterialSharedAxis

/**
 * Extension function to add enter and exit shared transition in Y axis on fragment
 */
fun <T: Fragment> T.addEnterExitSharedAxisTransition() {
    enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
    returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
}