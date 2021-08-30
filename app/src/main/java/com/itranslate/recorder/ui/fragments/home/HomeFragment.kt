package com.itranslate.recorder.ui.fragments.home

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
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
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*
import kotlin.concurrent.timer

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModel()

    private var audioDuration = "00:00"
    private var timerTask: Timer? = null
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null

    private var mStartRecording = true

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startRecordingProcess()
            } else {
                launchPermissionDialogRationale()
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareClickListeners()

    }

    /**
     * Function to prepare all click listeners
     */
    private fun prepareClickListeners() {
        binding.btnHomeMic.onClick {
            requestPermissionAudioRecording {
                startRecordingProcess()
            }
        }

        binding.btnHomeShowRecordings.onClick {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToRecordingsFragment()
            )
        }
    }

    private fun startRecordingProcess() {
        onRecord(createFile(), mStartRecording)
        binding.btnHomeMic.setImageResource(
            when (mStartRecording) {
                true -> R.drawable.ic_stop
                false -> R.drawable.ic_mic
            }
        )
        mStartRecording = !mStartRecording
    }

    private fun insertRecordInDb(audioFile: File) {
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
            )
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
                launchPermissionDialogRationale()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    private fun launchPermissionDialogRationale() {
        launchAlertDialog(
            getString(R.string.text_btn_open_settings),
            getString(R.string.dialog_title_error_permission_denied),
            getString(R.string.dialog_message_error_permission_denied)
        ) {
            launchSettingsIntent()
        }
    }

    private fun onRecord(audioFile: File, start: Boolean) = if (start) {
        startRecording(audioFile)
    } else {
        stopRecording(audioFile)
    }

    private fun onPlay(audioFile: File, start: Boolean) = if (start) {
        startPlaying(audioFile)
    } else {
        stopPlaying()
    }

    private fun startPlaying(audioFile: File) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(audioFile.path)
            prepare()
            setOnCompletionListener {
                if (isVisible) {
                    stopPlaying()
                }
            }
            start()
        }
    }

    private fun stopPlaying() {
        mediaPlayer?.release()
        mediaPlayer = null
        timerTask?.cancel()
        timerTask = null
    }

    private fun startRecording(audioFile: File) {
        resetMediaRecorder()
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(audioFile.path)
            prepare()
            start()
            runRecordTimerTask()
        }
    }

    private fun runRecordTimerTask() {
        var remainingTimeInSeconds = -1
        timerTask?.cancel()
        timerTask = timer(period = 1000) {
            remainingTimeInSeconds += 1

            val minutes = remainingTimeInSeconds % 3600 / 60
            val seconds = remainingTimeInSeconds % 60

            audioDuration = String.format("%02d:%02d", minutes, seconds)
        }
    }

    private fun stopRecording(audioFile: File) {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null
        insertRecordInDb(audioFile)
    }

    override fun onStop() {
        super.onStop()
        resetMediaRecorder()
        resetMediaPlayer()
    }

    private fun resetMediaRecorder() {
        mediaRecorder?.release()
        mediaRecorder = null
        timerTask?.cancel()
        timerTask = null
        audioDuration = "00:00"
    }

    private fun resetMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}