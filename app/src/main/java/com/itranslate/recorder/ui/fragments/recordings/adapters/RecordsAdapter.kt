package com.itranslate.recorder.ui.fragments.recordings.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.itranslate.recorder.data.local.models.records.Record
import com.itranslate.recorder.databinding.ItemRecordingBinding
import com.itranslate.recorder.general.viewholders.ClickableViewHolder
import com.itranslate.recorder.ui.fragments.recordings.viewholders.RecordViewHolder

class RecordsAdapter(
    private val clickCallback: (
        view: View,
        position: Int,
        clickType: ClickableViewHolder.ClickType<Record>
    ) -> Unit
) : PagingDataAdapter<Record, RecordViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        return RecordViewHolder(
            ItemRecordingBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickCallback
        )
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Record>() {
        override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
            return newItem.recordName.equals(oldItem.recordName, ignoreCase = false)
        }
    }
}