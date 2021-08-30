package com.itranslate.recorder.general.extensions

import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.itranslate.recorder.R
import com.itranslate.recorder.general.Constants.FILE_NAME_PATTERN
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Extension function to add enter and exit shared transition in Y axis on fragment
 */
fun <T : Fragment> T.addEnterExitSharedAxisTransition() {
    enterTransition = MaterialSharedAxis(MaterialSharedAxis.Y, true)
    returnTransition = MaterialSharedAxis(MaterialSharedAxis.Y, false)
}

/**
 * Extension function to get drawable references easier
 */
fun <T : Fragment> T.getDrawable(@DrawableRes drawableResourceId: Int) =
    ContextCompat.getDrawable(requireContext(), drawableResourceId)

/**
 * Extension function to launch a confirmation dialog
 */
fun <T : Fragment> T.launchConfirmationDialog(
    title: String,
    message: String,
    callback: (DialogResult) -> Unit
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(getString(R.string.text_btn_no)) { dialog, _ ->
            dialog.dismiss()
            callback.invoke(DialogResult.Negative)
        }
        .setPositiveButton(getString(R.string.text_btn_yes)) { dialog, _ ->
            dialog.dismiss()
            callback.invoke(DialogResult.Positive)
        }
        .show()
}

/**
 * Sealed class representing dialog response types
 */
sealed class DialogResult {
    object Negative : DialogResult()
    object Positive : DialogResult()
}

/**
 * Extension function launching alert dialog
 */
fun <T : Fragment> T.launchAlertDialog(
    btnText: String,
    title: String,
    message: String,
    callback: () -> Unit
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(btnText) { dialog, _ ->
            dialog.dismiss()
            callback.invoke()
        }
        .show()
}

/**
 * Extension function to launch app settings
 * To be used after permission denied dialog is shown
 */
fun <T : Fragment> T.launchSettingsIntent() {
    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", requireActivity().packageName, null)
    })
}

/**
 * Extension function to create a temporary file
 */
fun <T : Fragment> T.createFile(): File {
    val storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC)
    val timeStamp: String = SimpleDateFormat(
        FILE_NAME_PATTERN,
        Locale.getDefault()
    ).format(Date())
    return File(storageDir, "$timeStamp.mp3")
}

/**
 * Extension function to show snack bar
 * registers callback
 * invoke [actionCallback] callback when [actionText] is provided
 */
fun <T : Fragment> T.showSnackBar(
    message: String,
    actionText: String? = null,
    callback: (SnackBarCallBack) -> Unit
) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).apply {
        if (actionText == null) {
            return@apply
        }
        setAction(actionText) {
            callback.invoke(SnackBarCallBack.Action)
        }
        addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>(){
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                when (event) {
                    DISMISS_EVENT_ACTION -> callback.invoke(SnackBarCallBack.Action)
                    DISMISS_EVENT_TIMEOUT -> callback.invoke(SnackBarCallBack.Dismiss)
                    else -> {}
                }
            }
        })
    }.show()
}

/**
 * Snackbar callback types
 */
sealed class SnackBarCallBack {
    object Action : SnackBarCallBack()
    object Dismiss : SnackBarCallBack()
}

