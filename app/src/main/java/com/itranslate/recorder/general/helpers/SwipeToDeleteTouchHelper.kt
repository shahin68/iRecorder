package com.itranslate.recorder.general.helpers

import android.graphics.Canvas
import android.graphics.Color
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.roundToLong


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
        viewHolder.itemView.background
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
        // calculating item view width
        val itemWidth = viewHolder.itemView.width

        // converting [dX] to absolute value
        val absoluteDx = abs(dX)

        // calculating alpha value which is between 0.0 and 1.0
        val alphaValue = (absoluteDx / itemWidth).toDouble()

        // setting background color to item view when it's moving
        viewHolder.itemView.setBackgroundColor(Color.parseColor(addAlphaToGreyColor(alphaValue)))

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    /**
     * Function to add alpha to HEX grey color #e2e2e2
     * @param alpha is value between 0.0 to 1.0
     * @return [String] HEX color string
     */
    private fun addAlphaToGreyColor(alpha: Double): String? {
        val originalColor = "#e2e2e2"
        val alphaFixed = (alpha * 255).roundToLong()
        var alphaHex = java.lang.Long.toHexString(alphaFixed)
        if (alphaHex.length == 1) {
            alphaHex = "0$alphaHex"
        }
        return originalColor.replace("#", "#$alphaHex")
    }

}