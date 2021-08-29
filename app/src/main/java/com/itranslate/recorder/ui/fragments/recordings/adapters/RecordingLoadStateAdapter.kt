package com.itranslate.recorder.ui.fragments.recordings.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.itranslate.recorder.databinding.ItemLoadStateBinding
import com.itranslate.recorder.ui.fragments.recordings.viewholders.RecordingLoadStateViewHolder

/**
 * Adapter for displaying a RecyclerView item based on LoadState, such as a loading spinner, or a
 * retry error button
 */
class RecordingLoadStateAdapter : LoadStateAdapter<RecordingLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: RecordingLoadStateViewHolder, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RecordingLoadStateViewHolder {
        return RecordingLoadStateViewHolder(
            ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}