package com.shyber.glass.sample.apidemo.card

import android.os.Bundle
import com.shyber.glass.sample.apidemo.R
import com.shyber.glass.sample.apidemo.base.BaseCardScrollActivity
import java.util.*

/**
 * Creates a card scroll view that shows an example of using a custom embedded layout in a
 * `CardBuilder`.
 */
class EmbeddedCardLayoutActivity : BaseCardScrollActivity() {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        mAdapter = EmbeddedCardLayoutAdapter(this, createItems())
        mCardScroller?.adapter = mAdapter
    }

    /** Creates some sample items that will be displayed on cards in the card scroll view.  */
    private fun createItems(): List<SimpleTableItem> {
        val items = ArrayList<SimpleTableItem>()
        items.add(SimpleTableItem(R.drawable.ic_circle_blue, "Water", "8 oz"))
        items.add(SimpleTableItem(R.drawable.ic_circle_yellow, "Eggs, large", "2"))
        items.add(SimpleTableItem(R.drawable.ic_circle_red, "Ground beef", "4 oz"))
        items.add(SimpleTableItem(R.drawable.ic_circle_green, "Brussel sprouts", "1 cup"))
        items.add(SimpleTableItem(R.drawable.ic_circle_green, "Celery", "1 stalk"))
        items.add(SimpleTableItem(R.drawable.ic_circle_red, "Beef jerky", "8 strips"))
        items.add(SimpleTableItem(R.drawable.ic_circle_yellow, "Almonds", "3 handfuls"))
        items.add(
            SimpleTableItem(
                R.drawable.ic_circle_red, "Strawberry fruit leather", "2.5 miles"
            )
        )
        return items
    }
}