package com.itranslate.recorder.general.extensions

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * Extension function to verify whether bottom sheet is expanded or not
 */
fun <T : BottomSheetBehavior<View>> T.isExpanded() = state == BottomSheetBehavior.STATE_EXPANDED

/**
 * Extension function to expand
 */
fun <T : BottomSheetBehavior<View>> T.expand() {
    state = BottomSheetBehavior.STATE_EXPANDED
}

/**
 * Extension function to collapnse
 */
fun <T : BottomSheetBehavior<View>> T.collapse() {
    state = BottomSheetBehavior.STATE_COLLAPSED
}