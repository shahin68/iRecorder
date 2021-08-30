package com.itranslate.recorder.ui.fragments.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.itranslate.recorder.R
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.databinding.FragmentHomeBinding
import com.itranslate.recorder.general.Constants.GENERIC_FILE_NAME
import com.itranslate.recorder.general.extensions.*
import com.itranslate.recorder.ui.fragments.BaseFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var voiceRecorderSheet: BottomSheetBehavior<View>

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                addRecording()
            } else {
                launchPermissionDesc()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareClickListeners()

        initVoiceRecorderSheet()
    }

    /**
     * Function to prepare all click listeners
     */
    private fun prepareClickListeners() {
        binding.btnHomeMic.onClick {
            if (::voiceRecorderSheet.isInitialized) {
                if (!voiceRecorderSheet.isExpanded()) {
                    openSheet()
                }
            }
        }

        binding.btnHomeShowRecordings.onClick {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToRecordingsFragment()
            )
        }
    }

    private fun initVoiceRecorderSheet() {
        voiceRecorderSheet = BottomSheetBehavior.from(binding.layoutVoiceRecorder.root)
        voiceRecorderSheet.isDraggable = false

        binding.layoutVoiceRecorder.btnSheetRecord.setOnClickListener {

        }

        binding.layoutVoiceRecorder.icSheetClose.setOnClickListener {
            closeSheet()
        }

        binding.layoutVoiceRecorder.btnSheetSave.setOnClickListener {
            requestPermissionAudioRecording {
                addRecording()
            }
        }
    }

    private fun addRecording() {
        lifecycleScope.launch {
            viewModel.insertRecord(
                Record(recordName = GENERIC_FILE_NAME)
            )
            Toast.makeText(requireContext(), "Item added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openSheet() {
        if (::voiceRecorderSheet.isInitialized) {
            voiceRecorderSheet.expand()
        }
    }

    private fun closeSheet() {
        if (::voiceRecorderSheet.isInitialized) {
            voiceRecorderSheet.collapse()
        }
    }

    private fun requestPermissionAudioRecording(permissionGranted: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                permissionGranted.invoke()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                launchPermissionDesc()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun launchPermissionDesc() {
        launchAlertDialog(getString(R.string.text_btn_open_settings), getString(R.string.dialog_title_error_permission_denied), getString(R.string.dialog_message_error_permission_denied)) {
            launchSettingsIntent()
        }
    }
}