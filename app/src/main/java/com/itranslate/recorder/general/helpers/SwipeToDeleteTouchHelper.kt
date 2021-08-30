package com.itranslate.recorder.general.helpers

import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.itranslate.recorder.R


/**
 * Class is a swipe to delete touch helper for recycler view
 * Simplest implementation required just a callback where [onSwiped] is called
 * Drag & swipe directions are also defined as input to the class
 */
class SwipeToDeleteTouchHelper(
    dragDirections: Int,
    swipeDirections: Int,
    private val swipedCallback: (RecyclerView.ViewHolder) -> Unit
) : ItemTouchHelper.SimpleCallback(dragDirections, swipeDirections) {

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

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        // setting background color to item view when it's moving
        viewHolder.itemView.setBackgroundColor(
            ContextCompat.getColor(
                viewHolder.itemView.context,
                if (dX == 0f) {
                    R.color.transparent
                } else {
                    R.color.grey
                }
            )
        )
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

}