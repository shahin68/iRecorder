package com.itranslate.recorder.ui.fragments.recordings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.itranslate.recorder.R
import com.itranslate.recorder.databinding.FragmentRecordingsBinding
import com.itranslate.recorder.general.extensions.addEnterExitSharedAxisTransition
import com.itranslate.recorder.general.extensions.visibleOrGone
import com.itranslate.recorder.ui.fragments.BaseFragment
import com.itranslate.recorder.ui.fragments.recordings.adapters.RecordingLoadStateAdapter
import com.itranslate.recorder.ui.fragments.recordings.adapters.RecordsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecordingsFragment :
    BaseFragment<FragmentRecordingsBinding>(FragmentRecordingsBinding::inflate) {

    private val viewModel: RecordingsViewModel by viewModels()
    private val recordsAdapter = RecordsAdapter { view, position, clickType ->

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
        binding.recordingsAdapter = recordsAdapter.apply {
            withLoadStateFooter(footer = RecordingLoadStateAdapter())
        }
        recordsAdapter.addLoadStateListener { combinedState ->
            if (isAdded) {
                when (val state = combinedState.append) {
                    is LoadState.NotLoading -> {
                        onLoading(show = false)
                        showEmptyView(
                            show = recordsAdapter.itemCount < 1,
                            getString(R.string.error_message_no_recording)
                        )
                    }
                    is LoadState.Loading -> {
                        onLoading(show = !state.endOfPaginationReached)
                    }
                    is LoadState.Error -> {
                        onLoading(show = false)
                        showEmptyView(
                            show = true, state.error.localizedMessage
                        )
                    }
                }
                when (val state = combinedState.refresh) {
                    is LoadState.Loading -> {
                        onLoading(show = !state.endOfPaginationReached)
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
     * Method to show or hide loading view
     */
    private fun onLoading(show: Boolean) = when {
        show -> {
            binding.progressbarRecordingsLoading.show()
        }
        else -> {
            binding.progressbarRecordingsLoading.hide()
        }
    }

    private fun observeRecordings() {
        lifecycleScope.launch {
            viewModel.getSortedRecordings().collectLatest {
                recordsAdapter.submitData(it)
            }
        }
    }

}