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
 * Extension function to register bottom sheet callback
 * invokes [onCollapseCallback] after bottom sheet has collapsed
 */
fun <T : BottomSheetBehavior<View>> T.registerCallback(onCollapseCallback: () -> Unit) {
    addBottomSheetCallback(object :BottomSheetBehavior.BottomSheetCallback(){
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                onCollapseCallback.invoke()
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {

        }
    })
}
