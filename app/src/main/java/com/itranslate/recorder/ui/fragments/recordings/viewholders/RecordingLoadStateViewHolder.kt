package com.itranslate.recorder.ui.fragments.recordings.viewholders

import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.itranslate.recorder.databinding.ItemLoadStateBinding
import com.itranslate.recorder.general.extensions.visibleOrGone

/**
 * View holder representing item based on [LoadState]
 */
class RecordingLoadStateViewHolder(
    private val binding: ItemLoadStateBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bindState(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.tvMessage.text = loadState.error.localizedMessage
        }

        binding.progressbarLoading.visibleOrGone(loadState is LoadState.Loading)
        binding.tvMessage.visibleOrGone(loadState is LoadState.Error)
    }

}