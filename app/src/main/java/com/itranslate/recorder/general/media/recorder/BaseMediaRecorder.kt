package com.itranslate.recorder.general.media.recorder

import android.media.MediaRecorder

class BaseMediaRecorder(
    private val recorderCallback: (RecorderEvent) -> Unit
): MediaRecorder() {

    fun startRecording(filePath: String) {
        setAudioSource(AudioSource.MIC)
        setOutputFormat(OutputFormat.AAC_ADTS)
        setAudioEncoder(AudioEncoder.AAC)
        setOutputFile(filePath)
        prepare()
        start()
        recorderCallback.invoke(RecorderEvent.Start)
    }

    fun stopRecording() {
        stop()
        release()
        recorderCallback.invoke(RecorderEvent.End)
    }

}