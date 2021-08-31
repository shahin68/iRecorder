package com.itranslate.recorder.ui.fragments.home

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.itranslate.recorder.R
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.databinding.FragmentHomeBinding
import com.itranslate.recorder.general.Constants.GENERIC_FILE_NAME
import com.itranslate.recorder.general.extensions.*
import com.itranslate.recorder.ui.fragments.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*
import kotlin.concurrent.timer

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()

    private val micBtnInactiveDuration = 1000L

    private lateinit var audioFile: File
    private var audioDuration = "00:00"
    private var isRecording = false
    private var mediaRecorder: MediaRecorder? = null
    private var timerTask: Timer? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startOrStopRecording {
                    disableMicBtnForInactiveDuration()
                }
            } else {
                launchPermissionDialogRationale()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareClickListeners()

    }

    override fun onDestroy() {
        super.onDestroy()
        resetMediaRecorder()
    }

    /**
     * Function to prepare all click listeners
     */
    private fun prepareClickListeners() {
        binding.btnHomeMic.onClick {
            requestPermissionAudioRecording {
                startOrStopRecording {
                    disableMicBtnForInactiveDuration()
                }
            }
        }

        binding.btnHomeShowRecordings.onClick {
            if (mediaRecorder != null) {
                launchConfirmationDialog(
                    getString(R.string.dialog_title_error_recording_in_process),
                    getString(R.string.dialog_message_error_recording_in_process)
                ) { dialogResult ->
                    when (dialogResult) {
                        DialogResult.Negative -> {
                            stopRecording()
                            isRecording = !isRecording
                        }
                        DialogResult.Positive -> {
                            // do nothing
                        }
                    }
                }
                return@onClick
            }
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToRecordingsFragment()
            )
        }
    }

    /**
     * Disable mic button activation for an inactive duration of [micBtnInactiveDuration]
     */
    private fun disableMicBtnForInactiveDuration() {
        binding.btnHomeMic.isEnabled = false
        lifecycleScope.launch {
            delay(micBtnInactiveDuration)
            binding.btnHomeMic.isEnabled = true
        }
    }

    /**
     * function to request audio recording permission
     */
    private fun requestPermissionAudioRecording(permissionGranted: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                permissionGranted.invoke()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                launchPermissionDialogRationale()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    /**
     * launch permission dialog when permission is denied
     * dialog will elaborate on the permission
     * user will be redirected to app setting
     */
    private fun launchPermissionDialogRationale() {
        launchAlertDialog(
            getString(R.string.text_btn_open_settings),
            getString(R.string.dialog_title_error_permission_denied),
            getString(R.string.dialog_message_error_permission_denied)
        ) {
            launchSettingsIntent()
        }
    }

    /**
     * function to start running recording process
     */
    private fun startOrStopRecording(onStartRecording: () -> Unit) {
        when (isRecording) {
            true -> {
                stopRecording()
            }
            false -> {
                audioFile = createFile()
                startRecording()
                onStartRecording.invoke()
            }
        }
        isRecording = !isRecording
    }

    /**
     * function to start and instantiate audio recording
     */
    private fun startRecording() {
        if (::audioFile.isInitialized) {
            binding.btnHomeMic.setImageResource(R.drawable.ic_stop)
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(audioFile.path)
                prepare()
                start()
                runRecordTimerTask()
                disableScreenRotation()
            }
        }
    }

    /**
     * function to keep track of audio duration
     */
    private fun runRecordTimerTask() {
        var remainingTimeInSeconds = -1
        timerTask?.cancel()
        timerTask = timer(period = 1000) {
            remainingTimeInSeconds += 1

            val minutes = remainingTimeInSeconds % 3600 / 60
            val seconds = remainingTimeInSeconds % 60

            val duration = String.format("%02d:%02d", minutes, seconds)

            Log.i(this@HomeFragment::class.simpleName, duration)
            audioDuration = duration
        }
    }

    /**
     * function to stop stop the recording
     */
    private fun stopRecording() {
        binding.btnHomeMic.setImageResource(R.drawable.ic_mic)
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        timerTask?.cancel()
        timerTask = null
        insertRecordInDb()
        enableScreenRotation()
    }

    /**
     * insert record into database after record is done and stopped
     */
    private fun insertRecordInDb() {
        if (::audioFile.isInitialized) {
            Log.d(this::class.simpleName, audioFile.path)
            lifecycleScope.launch {
                viewModel.insertRecordInDb(
                    Record(
                        recordName = GENERIC_FILE_NAME,
                        recordPath = audioFile.path,
                        recordDuration = audioDuration
                    )
                )
                showSnackBar(
                    message = getString(
                        R.string.snackbar_message_record_added,
                        audioFile.name
                    )
                ) {}
            }
        }
    }

    /**
     * stop and reset [mediaRecorder] & [timerTask] & [audioDuration]
     */
    private fun resetMediaRecorder() {
        mediaRecorder?.release()
        mediaRecorder = null
        timerTask?.cancel()
        timerTask = null
        audioDuration = "00:00"
    }

    /**
     * Disable screen rotation
     * To be used only when a recording is in proccess
     */
    private fun disableScreenRotation() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
    }

    /**
     * Enable screen rotation
     * To be used after recording process is finished
     */
    private fun enableScreenRotation() {
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

}