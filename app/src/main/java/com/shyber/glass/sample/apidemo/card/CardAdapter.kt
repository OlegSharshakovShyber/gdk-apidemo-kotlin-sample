package com.shyber.glass.sample.apidemo.card

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.google.android.glass.widget.CardBuilder
import com.google.android.glass.widget.CardScrollAdapter

/**
 * Adapter class that handles list of cards.
 */
open class CardAdapter(val mCards: ArrayList<CardBuilder>) :
    CardScrollAdapter() {
    override fun getCount(): Int {
        return mCards.size
    }

    override fun getItem(position: Int): Any {
        return mCards[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return mCards[position].getView(convertView, parent)
    }

    override fun getViewTypeCount(): Int {
        return CardBuilder.getViewTypeCount()
    }

    override fun getItemViewType(position: Int): Int {
        return mCards[position].itemViewType
    }

    override fun getPosition(item: Any): Int {
        for (i in mCards.indices) {
            if (getItem(i) == item) {
                return i
            }
        }
        return AdapterView.INVALID_POSITION
    }

}