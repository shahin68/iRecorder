package com.itranslate.recorder.ui.fragments.recordings

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.ItemTouchHelper
import com.itranslate.recorder.R
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.databinding.FragmentRecordingsBinding
import com.itranslate.recorder.general.extensions.SnackBarCallBack
import com.itranslate.recorder.general.extensions.addEnterExitSharedAxisTransition
import com.itranslate.recorder.general.extensions.showSnackBar
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
    private val recordsAdapter = RecordsAdapter { _, position, clickType ->
        when (clickType) {
            is ClickableViewHolder.ClickType.ToOpen -> {
                val path = clickType.data.recordPath
                Log.d(this@RecordingsFragment::class.simpleName, path)
                startMediaPlayer(path)
            }
        }
    }
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addEnterExitSharedAxisTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecordingsList()

        observeRecordings()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true) {
            findNavController().popBackStack()
        }
    }

    /**
     * invalidate [mediaPlayer] when fragment is destroyed and not visible anymore
     */
    override fun onDestroy() {
        super.onDestroy()
        invalidateMediaPlayer()
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
            }

            launchUndoRecordDeletionSnackBar(record = swipedRecord)

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
     * function to show undo deletion snack bar
     */
    private fun launchUndoRecordDeletionSnackBar(record: Record) {
        showSnackBar(
            getString(
                R.string.snackbar_message_record_removed,
                record.recordName
            ), getString(R.string.text_btn_undo)
        ) {
            when (it) {
                SnackBarCallBack.Action -> {
                    lifecycleScope.launch {
                        viewModel.insertRecordInDb(record)
                    }
                }
                SnackBarCallBack.Dismiss -> File(record.recordPath).delete()
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
     * function to start playing audio record
     */
    private fun startMediaPlayer(audioPath: String) {
        if (mediaPlayer != null) {
            stopMediaPlayer()
        }
        mediaPlayer = MediaPlayer().apply {
            try {
                setDataSource(audioPath)
                prepare()
                setOnCompletionListener {
                    if (isVisible) {
                        invalidateMediaPlayer()
                    }
                }
                start()
            } catch (e: Exception) {

            }
        }
    }

    private fun stopMediaPlayer() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }

    /**
     * function to stop playing audio record
     */
    private fun invalidateMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

}