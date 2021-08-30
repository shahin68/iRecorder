package com.itranslate.recorder.ui.fragments.recordings.viewholders

import android.view.View
import androidx.core.content.ContextCompat
import com.itranslate.recorder.R
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.databinding.ItemRecordingBinding
import com.itranslate.recorder.general.viewholders.ClickableViewHolder

/**
 * Record item view holder
 */
class RecordViewHolder(
    private val binding: ItemRecordingBinding,
    clickCallback: (
        view: View,
        position: Int,
        clickType: ClickType<Record>
    ) -> Unit
) : ClickableViewHolder<Record>(binding.root, clickCallback) {

    override fun bind(item: Record) {
        super.bind(item)
        binding.record = item
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.transparent))
    }

}