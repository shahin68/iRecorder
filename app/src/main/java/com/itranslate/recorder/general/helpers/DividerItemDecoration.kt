package com.itranslate.recorder.general.helpers

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.itranslate.recorder.R

/**
 * Custom item decoration used to draw divider between all items expect the last one
 */
class DividerItemDecoration(context: Context): RecyclerView.ItemDecoration() {

    private val divider = ContextCompat.getDrawable(context, R.drawable.divider_horizontal)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        divider?.let {
            for (i in 0 until parent.childCount) {
                val child = parent.getChildAt(i)
                it.setBounds(child.left, child.bottom, child.width, child.bottom + (divider.intrinsicHeight))
                it.draw(c)
            }
        }
    }

}