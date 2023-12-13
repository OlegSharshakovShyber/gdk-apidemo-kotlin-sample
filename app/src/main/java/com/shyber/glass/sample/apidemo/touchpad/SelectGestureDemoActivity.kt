package com.shyber.glass.sample.apidemo.touchpad

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import com.google.android.glass.widget.CardBuilder
import com.shyber.glass.sample.apidemo.R
import com.shyber.glass.sample.apidemo.base.BaseCardScrollActivity
import java.util.*

/**
 * Lets the user select which gesture detector demo they want to run.
 */
class SelectGestureDemoActivity : BaseCardScrollActivity() {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setCardScrollerListener()
    }

    /**
     * Create the list of cards that represent the available gesture detector demos.
     */
    override fun createCards(context: Context): ArrayList<CardBuilder> {
        val cards = ArrayList<CardBuilder>()
        cards.add(
            DISCRETE,
            CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.discrete_gestures)
        )
        cards.add(
            CONTINUOUS,
            CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.continuous_gestures)
        )
        return cards
    }

    /**
     * Sets the click listener that invokes a gesture detector demo based on the card that is
     * selected.
     */
    private fun setCardScrollerListener() {
        mCardScroller?.onItemClickListener = OnItemClickListener { _, _, position, id ->
            Log.d(
                TAG,
                "Clicked view at position $position, row-id $id"
            )
            when (position) {
                DISCRETE -> startActivity(
                    Intent(
                        this@SelectGestureDemoActivity,
                        DiscreteGesturesActivity::class.java
                    )
                )
                CONTINUOUS -> startActivity(
                    Intent(
                        this@SelectGestureDemoActivity,
                        ContinuousGesturesActivity::class.java
                    )
                )
                else -> Log.d(
                    TAG,
                    "Don't show anything"
                )
            }
        }
    }

    companion object {
        private val TAG = SelectGestureDemoActivity::class.java.simpleName

        // Index of the gesture detector demos.
        private const val DISCRETE = 0
        private const val CONTINUOUS = 1
    }
}