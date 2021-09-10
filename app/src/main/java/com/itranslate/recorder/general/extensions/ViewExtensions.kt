package com.itranslate.recorder.general.extensions

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.ProgressBar
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator

/**
 * extension function to set view visibility as VISIBLE
 */
fun <T : View> T.visible() {
    visibility = View.VISIBLE
}

/**
 * extension function to set view visibility as GONE
 */
fun <T : View> T.gone() {
    visibility = View.GONE
}

/**
 * extension function to set view visibility as VISIBLE or GONE
 */
fun <T : View> T.visibleOrGone(visible: Boolean) {
    if (visible) {
        visible()
    } else {
        gone()
    }
}

/**
 * extension function to register click listener on a view
 */
fun <T : View> T.onClick(block: (View) -> Unit) {
    return setOnClickListener {
        block.invoke(this)
    }
}

/**
 * Extension function to show or hide [CircularProgressIndicator]
 * show() & hide() belong the the view where they trigger show & hide animation behaviors
 */
fun CircularProgressIndicator.showOrHide(show: Boolean) = when {
    show -> {
        show()
    }
    else -> {
        hide()
    }
}

/**
 * Extension function to show or hide [LinearProgressIndicator]
 * show() & hide() belong the the view where they trigger show & hide animation behaviors
 */
fun LinearProgressIndicator.showOrHide(show: Boolean) = when {
    show -> {
        show()
    }
    else -> {
        hide()
    }
}

/**
 * Extension function to play animated vector drawable
 * Providing callback events
 */
fun AppCompatImageView.startVectorAnimation(
    @DrawableRes animatedVectorId: Int
) {
    setImageResource(animatedVectorId)
    (drawable as Animatable).apply {
        AnimatedVectorDrawableCompat.registerAnimationCallback(
            drawable,
            object : Animatable2Compat.AnimationCallback() {
                override fun onAnimationStart(drawable: Drawable?) {
                    isEnabled = false
                }

                override fun onAnimationEnd(drawable: Drawable?) {
                    isEnabled = true
                }
            })
    }.run { start() }
}

/**
 * Extension function to set progress value of progressbar
 * Where on Api level higher than 24 animation is supported which helps to observe smooth progress change
 */
fun ProgressBar.setProgressValue(currentProgress: Int) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    setProgress(currentProgress, true)
} else {
    progress = currentProgress
}

