package com.itranslate.recorder.general.extensions

import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.transition.platform.MaterialSharedAxis

/**
 * Extension function to add enter and exit shared transition in Y axis on fragment
 */
fun <T : Fragment> T.addEnterExitSharedAxisTransition() {
    enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
    returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
}

/**
 * Extension function to get drawable references easier
 */
fun <T : Fragment> T.getDrawable(@DrawableRes drawableResourceId: Int) =
    ContextCompat.getDrawable(requireContext(), drawableResourceId)