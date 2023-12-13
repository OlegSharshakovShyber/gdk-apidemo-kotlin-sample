package com.shyber.glass.sample.apidemo.card

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import com.google.android.glass.widget.CardBuilder
import com.google.android.glass.widget.CardScrollAdapter
import com.shyber.glass.sample.apidemo.R
import kotlin.math.ceil
import kotlin.math.min

/**
 * Populates views in a `CardScrollView` with cards built from custom embedded layouts to
 * represent items in a simple table.
 */
class EmbeddedCardLayoutAdapter
/** Initializes a new adapter with the specified context and list of items.  */(
    private val mContext: Context,
    private val mItems: List<SimpleTableItem>
) : CardScrollAdapter() {
    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getCount(): Int {
        // Compute the number of cards needed to display the items with 4 per card (rounding up to
        // capture the remainder).
        return ceil(mItems.size.toDouble() / ITEMS_PER_CARD).toInt()
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getPosition(item: Any): Int {
        return AdapterView.INVALID_POSITION
    }

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val card = CardBuilder(mContext, CardBuilder.Layout.EMBED_INSIDE)
            .setEmbeddedLayout(R.layout.simple_table)
            .setFootnote(R.string.text_card_embedded_footnote)
            .setTimestamp(R.string.text_card_embedded_timestamp)
        val view = card.getView(convertView, parent)

        // Get a reference to an embedded view from the custom layout and then manipulate it.
        val tableView = view.findViewById(R.id.simple_table) as ViewGroup
        populateTableRows(position, tableView)
        return view
    }

    /** Populates all of the rows in the card at the specified position.  */
    private fun populateTableRows(position: Int, tableView: ViewGroup) {
        val startItemIndex = position * ITEMS_PER_CARD
        val endItemIndex = min(
            startItemIndex + ITEMS_PER_CARD,
            mItems.size
        )
        for (i in 0 until ITEMS_PER_CARD) {
            val itemIndex = startItemIndex + i
            val rowView = tableView.getChildAt(i) as ViewGroup

            // The layout contains four fixed rows, so we need to hide the later ones if there are
            // not four items on this card. We need to make sure to update the visibility in both
            // cases though if the card has been recycled.
            if (itemIndex < endItemIndex) {
                val item = mItems[itemIndex]
                populateTableRow(item, rowView)
                rowView.visibility = View.VISIBLE
            } else {
                rowView.visibility = View.INVISIBLE
            }
        }
    }

    /** Populates a row in the table with the specified item data.  */
    private fun populateTableRow(item: SimpleTableItem, rowView: ViewGroup) {
        val imageView =
            rowView.getChildAt(IMAGE_VIEW_INDEX) as ImageView
        val primaryTextView =
            rowView.getChildAt(PRIMARY_TEXT_VIEW_INDEX) as TextView
        val secondaryTextView =
            rowView.getChildAt(SECONDARY_TEXT_VIEW_INDEX) as TextView
        imageView.setImageResource(item.iconResId)
        primaryTextView.text = item.primaryText
        secondaryTextView.text = item.secondaryText
    }

    companion object {
        /** The maximum number of items that fit on a card.  */
        private const val ITEMS_PER_CARD = 4

        /** Index of the [ImageView] containing the icon in a table row.  */
        private const val IMAGE_VIEW_INDEX = 0

        /** Index of the [TextView] containing the primary text in a table row.  */
        private const val PRIMARY_TEXT_VIEW_INDEX = 1

        /** Index of the [TextView] containing the secondary text in a table row.  */
        private const val SECONDARY_TEXT_VIEW_INDEX = 2
    }

}