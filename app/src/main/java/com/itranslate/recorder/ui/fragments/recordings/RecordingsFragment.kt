package com.itranslate.recorder.ui.fragments.recordings

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ItemTouchHelper
import com.itranslate.recorder.R
import com.itranslate.recorder.databinding.FragmentRecordingsBinding
import com.itranslate.recorder.general.extensions.addEnterExitSharedAxisTransition
import com.itranslate.recorder.general.extensions.visibleOrGone
import com.itranslate.recorder.general.helpers.DividerItemDecoration
import com.itranslate.recorder.general.helpers.SwipeToDeleteTouchHelper
import com.itranslate.recorder.general.viewholders.ClickableViewHolder
import com.itranslate.recorder.ui.fragments.BaseFragment
import com.itranslate.recorder.ui.fragments.recordings.adapters.RecordingLoadStateAdapter
import com.itranslate.recorder.ui.fragments.recordings.adapters.RecordsAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class RecordingsFragment :
    BaseFragment<FragmentRecordingsBinding>(FragmentRecordingsBinding::inflate) {

    private val viewModel: RecordingsViewModel by viewModel()
    private var mediaPlayer: MediaPlayer? = null
    private val recordsAdapter = RecordsAdapter { _, position, clickType ->
        when (clickType) {
            is ClickableViewHolder.ClickType.ToOpen -> {
                val path = clickType.data.recordPath
                Log.d(this@RecordingsFragment::class.simpleName, path)
                startPlaying(path)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addEnterExitSharedAxisTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecordingsList()

        observeRecordings()

    }

    private fun setupRecordingsList() {
        // set item decoration
        binding.rvRecordingsList.addItemDecoration(DividerItemDecoration(requireContext()))

        // set item touch helper for swipe to delete action
        ItemTouchHelper(SwipeToDeleteTouchHelper(0, ItemTouchHelper.LEFT) {

            val swipedRecord =
                recordsAdapter.peek(it.bindingAdapterPosition) ?: return@SwipeToDeleteTouchHelper

            lifecycleScope.launch {
                viewModel.deleteRecord(swipedRecord)
                File(swipedRecord.recordPath).delete()
            }

        }).attachToRecyclerView(binding.rvRecordingsList)

        // set adapter with load state footer & load state listener
        binding.recordingsAdapter = recordsAdapter.apply {
            withLoadStateFooter(footer = RecordingLoadStateAdapter())
            addLoadStateListener { combinedState ->
                if (isAdded) {
                    binding.combinedLoadStates = combinedState
                    when (val state = combinedState.append) {
                        is LoadState.NotLoading -> {
                            showEmptyView(
                                show = recordsAdapter.itemCount < 1,
                                getString(R.string.error_message_no_recording)
                            )
                        }
                        is LoadState.Error -> {
                            showEmptyView(
                                show = true, state.error.localizedMessage
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Show error message or no data message when recordings list is empty
     */
    private fun showEmptyView(show: Boolean, errorMessage: String?) {
        binding.tvError.visibleOrGone(show)
        lifecycleScope.launch {
            delay(500)
            if (isAdded && isVisible) {
                binding.tvError.text = errorMessage
            }
        }
    }

    /**
     * instantiate received records observer
     * A flow of record table will be collected where all changes are seemingly submitted to the [recordsAdapter]
     */
    private fun observeRecordings() {
        lifecycleScope.launch {
            viewModel.getSortedRecordings().collectLatest {
                recordsAdapter.submitData(it)
            }
        }
    }

    /**
     * function to start playing audio record
     */
    private fun startPlaying(audioPath: String) {
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(audioPath)
                prepare()
                setOnCompletionListener {
                    if (isVisible) {
                        stopPlaying()
                    }
                }
                start()
            } catch (e: Exception) {

            }
        }
    }

    /**
     * function to stop playing audio record
     */
    private fun stopPlaying() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}