package com.itranslate.recorder.general.extensions

import android.view.View
import androidx.core.view.isVisible
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.progressindicator.LinearProgressIndicator

/**
 * extension function to set view visibility as VISIBLE
 */
fun <T: View> T.visible() {
    visibility = View.VISIBLE
}

/**
 * extension function to set view visibility as GONE
 */
fun <T: View> T.gone() {
    visibility = View.GONE
}

/**
 * extension function to set view visibility as INVISIBLE
 */
fun <T: View> T.hide() {
    visibility = View.INVISIBLE
}

/**
 * extension function to set view visibility as VISIBLE or GONE
 */
fun <T: View> T.visibleOrGone(visible: Boolean) {
    if (visible) {
        visible()
    } else {
        gone()
    }
}

/**
 * extension function to toggle view visibility
 */
fun <T: View> T.toggleVisibleOrGone() {
    if (isVisible) {
        gone()
    } else {
        visible()
    }
}

/**
 * extension function to set view visibility as VISIBLE or INVISIBLE
 */
fun <T: View> T.visibleOrHide(show: Boolean) {
    if (show) {
        visible()
    } else {
        hide()
    }
}

/**
 * extension function to register click listener on a view
 */
fun <T: View> T.onClick(block: (View) -> Unit) {
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


