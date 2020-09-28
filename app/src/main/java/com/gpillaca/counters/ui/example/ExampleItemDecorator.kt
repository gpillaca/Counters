package com.gpillaca.counters.ui.example

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ExampleItemDecorator() : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val density = parent.context.resources.displayMetrics.density
        val padding = 17

        outRect.left = (density * padding).toInt()
    }
}