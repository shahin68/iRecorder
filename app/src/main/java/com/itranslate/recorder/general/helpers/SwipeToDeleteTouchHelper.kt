package com.itranslate.recorder.general.helpers

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Class is a swipe to delete touch helper for recycler view
 * Simplest implementation required just a callback where [onSwiped] is called
 * Drag & swipe directions are also defined as input to the class
 */
class SwipeToDeleteTouchHelper(
    dragDirections: Int,
    swipeDirections: Int,
    private val swipedCallback: (RecyclerView.ViewHolder) -> Unit
): ItemTouchHelper.SimpleCallback(dragDirections, swipeDirections) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        swipedCallback.invoke(viewHolder)
    }
}