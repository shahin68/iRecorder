package com.itranslate.recorder.general.media.player

import android.media.MediaPlayer

/**
 * Extended class of [MediaPlayer] to be used as friendly media player object
 * invokes [onCompletionCallback] on completion of a media player session
 */
class BaseMediaPlayer(
    private val onCompletionCallback: () -> Unit
): MediaPlayer() {

    fun startPlaying(filePath: String) {
        setDataSource(filePath)
        prepare()
        setOnCompletionListener {
            onCompletionCallback.invoke()
        }
        start()
    }

    fun stopPlaying() {
        stop()
        release()
    }

}