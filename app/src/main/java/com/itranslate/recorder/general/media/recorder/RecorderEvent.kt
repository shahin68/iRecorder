package com.itranslate.recorder.general.media.recorder

/**
 * Sealed class representing default events for [BaseMediaRecorder]
 */
sealed class RecorderEvent{
    object Start: RecorderEvent()
    object End: RecorderEvent()
}