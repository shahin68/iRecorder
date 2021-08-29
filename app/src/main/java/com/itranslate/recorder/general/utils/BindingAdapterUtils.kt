package com.itranslate.recorder.general.utils

import androidx.databinding.BindingAdapter
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.itranslate.recorder.general.extensions.showOrHide

class BindingAdapterUtils {
    companion object {
        @JvmStatic
        @BindingAdapter("app:circularProgressIndicator_setShowOrHide")
        fun setShowOrHide(circularProgressIndicator: CircularProgressIndicator, combinedStates: CombinedLoadStates) {
            when (val state = combinedStates.append) {
                is LoadState.NotLoading -> {
                    circularProgressIndicator.showOrHide(show = false)
                }
                is LoadState.Loading -> {
                    circularProgressIndicator.showOrHide(show = !state.endOfPaginationReached)
                }
                is LoadState.Error -> {
                    circularProgressIndicator.showOrHide(show = false)
                }
            }
            when (val state = combinedStates.refresh) {
                is LoadState.Loading -> {
                    circularProgressIndicator.showOrHide(show = !state.endOfPaginationReached)
                }
            }
        }

        @JvmStatic
        @BindingAdapter("app:linearProgressIndicator_setShowOrHide")
        fun setShowOrHide(linearProgressIndicator: LinearProgressIndicator, showLoading: Boolean) {
            linearProgressIndicator.showOrHide(showLoading)
        }
    }
}