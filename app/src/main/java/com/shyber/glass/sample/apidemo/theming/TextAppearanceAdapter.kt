package com.shyber.glass.sample.apidemo.theming

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.google.android.glass.widget.CardScrollAdapter
import com.shyber.glass.sample.apidemo.R

/**
 * Adapter class that displays examples of the default textAppearance styles on Glass.
 */
class TextAppearanceAdapter(context: Context?) : CardScrollAdapter() {
    enum class TextAppearanceLayout(val resourceID: Int) {
        LARGE(R.layout.text_appearances_large), MEDIUM(R.layout.text_appearances_medium), SMALL(R.layout.text_appearances_small);

    }

    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return TextAppearanceLayout.values().size
    }

    override fun getItem(position: Int): TextAppearanceLayout {
        return TextAppearanceLayout.values()[position]
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        return convertView ?: mInflater.inflate(getItem(position).resourceID, null)
    }

    override fun getPosition(item: Any): Int {
        for (i in TextAppearanceLayout.values().indices) {
            if (getItem(i) == item) {
                return i
            }
        }
        return AdapterView.INVALID_POSITION
    }

}