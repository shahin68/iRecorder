package com.itranslate.recorder.general.extensions

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.platform.MaterialSharedAxis
import com.itranslate.recorder.R

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
fun <T : Fragment> T.launchConfirmationDialog(title: String, message: String, callback: (DialogResult) -> Unit) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(getString(R.string.text_btn_cancel)) { dialog, _ ->
            dialog.dismiss()
            callback.invoke(DialogResult.Cancel)
        }
        .setPositiveButton(getString(R.string.text_btn_save)) { dialog, _ ->
            dialog.dismiss()
            callback.invoke(DialogResult.Save)
        }
        .show()
}

/**
 * Sealed class representing dialog response types
 */
sealed class DialogResult {
    object Cancel: DialogResult()
    object Save: DialogResult()
}

/**
 * Extension function launching alert dialog
 */
fun <T : Fragment> T.launchAlertDialog(btnText: String, title: String, message: String, callback: () -> Unit) {
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

