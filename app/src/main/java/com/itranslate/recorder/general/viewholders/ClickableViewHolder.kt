package com.itranslate.recorder.general.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Clickable ViewHolder
 * @param T is any data class representing content of the view holder
 * @param clickCallback is a callback returning item content
 */
class ClickableViewHolder<T: Any>(
    view: View,
    private val clickCallback: (
        view: View,
        position: Int,
        clickType: ClickType<T>
    ) -> Unit
) : RecyclerView.ViewHolder(view) {

    /**
     * Bind data with item view
     */
    fun bind(item: T) {
        itemView.setOnClickListener {
            clickCallback.invoke(
                it,
                bindingAdapterPosition,
                ClickType.ToOpen(item)
            )
        }
    }

    /**
     * Used to register click types where type parameter [T] is the product output of child classes
     * without being consumed until invoked by a callback
     * @param T is any type parameter restricting outputs
     * @see ClickableViewHolder
     */
    sealed class ClickType<out T> {
        /**
         * Action used as callback to provide moment of consumption for the type parameter [T] when
         * opening or viewing the content is desired
         */
        data class ToOpen<T>(val data: T) : ClickType<T>()

        /**
         * Action used as callback to provide moment of consumption for the type parameter [T] when
         * removing or disregarding the content is desired
         */
        data class ToRemove<T>(val data: T) : ClickType<T>()
    }
}