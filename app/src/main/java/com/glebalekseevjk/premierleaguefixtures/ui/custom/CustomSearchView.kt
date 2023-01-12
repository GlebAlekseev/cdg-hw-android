package com.glebalekseevjk.premierleaguefixtures.ui.custom

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.SearchView

class CustomSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SearchView(context, attrs) {
    private var onSearchViewCollapsedListener: (() -> Unit)? = null
    private var onSearchViewExpandedListener: (() -> Unit)? = null

    private var isExpanded: Boolean = true
    override fun onActionViewCollapsed() {
        onSearchViewCollapsedListener?.invoke()
        super.onActionViewCollapsed()
    }

    override fun onActionViewExpanded() {
        onSearchViewExpandedListener?.invoke()
        super.onActionViewExpanded()

    }

    fun setOnSearchViewCollapsedListener(listener: ()->Unit){
        onSearchViewCollapsedListener = listener
    }
}