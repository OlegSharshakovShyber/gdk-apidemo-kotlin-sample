package com.shyber.glass.sample.apidemo.card

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import com.google.android.glass.media.Sounds
import com.google.android.glass.widget.CardBuilder
import com.google.android.glass.widget.CardScrollView
import com.shyber.glass.sample.apidemo.R
import com.shyber.glass.sample.apidemo.base.BaseCardScrollActivity
import java.util.*
import kotlin.collections.ArrayList

/**
 * Creates a card scroll view with various examples of its API.
 */
class CardScrollViewActivity : BaseCardScrollActivity() {
    /** Actions associated with cards.  */
    private enum class Action(val textId: Int, val imageId: Int) {
        DELETION_HERE(
            R.string.text_card_tap_to_delete,
            R.drawable.codemonkey1
        ),
        NAVIGATION_TO_BEGIN(
            R.string.text_card_tap_to_navigate_begin,
            R.drawable.codemonkey2
        ),
        NAVIGATION_TO_END(
            R.string.text_card_tap_to_navigate_end,
            R.drawable.codemonkey3
        ),
        INSERTION_AT_BEGIN(
            R.string.text_card_tap_to_insert_begin,
            R.drawable.codemonkey4
        ),
        INSERTION_BEFORE(
            R.string.text_card_tap_to_insert_before,
            R.drawable.codemonkey5
        ),
        INSERTION_AFTER(
            R.string.text_card_tap_to_insert_after,
            R.drawable.codemonkey6
        ),
        INSERTION_AT_END(
            R.string.text_card_tap_to_insert_end,
            R.drawable.codemonkey7
        ),
        NO_ACTION(R.string.text_card_no_action, R.drawable.codemonkey8);

    }

    /**
     * Adapter class that handles list of cards with associated actions and
     * allows for mutations without notifying the adapter of the data change yet
     * (through method [.notifyDataSetChanged]). Useful to demonstrate
     * mutation animations.
     */
    private inner class CardAdapterWithMutations :
        CardAdapter(ArrayList()) {
        private val mActions: ArrayList<Action> = ArrayList()

        /** Inserts a card into the adapter, without notifying.  */
        fun insertCardWithoutNotification(
            position: Int,
            card: CardBuilder,
            action: Action
        ) {
            mCards.add(position, card)
            mActions.add(position, action)
        }

        /** Deletes card from the adapter, without notifying.  */
        fun deleteCardWithoutNotification(position: Int) {
            mCards.removeAt(position)
            mActions.removeAt(position)
        }

        /** Returns the action associated with the card at position.  */
        fun getActionAt(position: Int): Action {
            return mActions[position]
        }

    }

    private val mRandom = Random()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setupAdapter()
        setupClickListener()
        setContentView(mCardScroller)
    }

    /**
     * Sets up adapter.
     */
    private fun setupAdapter() {
        mAdapter = CardAdapterWithMutations()

        // Insert initial cards, one of each kind.
        for (i in 0..7) {
            val action =
                Action.values()[i]
            val card = CardBuilder(this, CardBuilder.Layout.COLUMNS)
            card.setText(action.textId).addImage(action.imageId)
            (mAdapter as? CardAdapterWithMutations)?.insertCardWithoutNotification(i, card, action)
        }

        // Setting adapter notifies the card scroller of new content.
        mCardScroller?.adapter = mAdapter
    }

    /**
     * Sets up click listener.
     */
    private fun setupClickListener() {
        mCardScroller?.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            val am =
                getSystemService(Context.AUDIO_SERVICE) as AudioManager
            when ((mAdapter as? CardAdapterWithMutations)?.getActionAt(position)) {
                Action.DELETION_HERE -> {
                    am.playSoundEffect(Sounds.TAP)
                    deleteCard(position)
                }
                Action.NAVIGATION_TO_BEGIN -> {
                    am.playSoundEffect(Sounds.TAP)
                    navigateToCard(0)
                }
                Action.NAVIGATION_TO_END -> {
                    mAdapter?.count?.let { count ->
                        am.playSoundEffect(Sounds.TAP)
                        navigateToCard(count - 1)
                    }
                }
                Action.INSERTION_AT_BEGIN -> {
                    am.playSoundEffect(Sounds.TAP)
                    insertNewCard(0)
                }
                Action.INSERTION_BEFORE -> {
                    am.playSoundEffect(Sounds.TAP)
                    insertNewCard(position)
                }
                Action.INSERTION_AFTER -> {
                    am.playSoundEffect(Sounds.TAP)
                    insertNewCard(position + 1)
                }
                Action.INSERTION_AT_END -> {
                    mAdapter?.count?.let { count ->
                        am.playSoundEffect(Sounds.TAP)
                        insertNewCard(count)
                    }
                }
                else -> am.playSoundEffect(Sounds.DISALLOWED)
            }
        }
    }

    /**
     * Deletes a card at the given position using proper insertion animation
     * (the card scroller will animate the old card from view).
     */
    private fun deleteCard(position: Int) {
        // Delete card in the adapter, but don't call notifyDataSetChanged() yet.
        // Instead, request proper animation for deleted card from card scroller,
        // which will notify the adapter at the right time during the animation.
        (mAdapter as? CardAdapterWithMutations)?.deleteCardWithoutNotification(position)
        mCardScroller?.animate(position, CardScrollView.Animation.DELETION)
    }

    /** Navigates to card at given position.  */
    private fun navigateToCard(position: Int) {
        mCardScroller?.animate(position, CardScrollView.Animation.NAVIGATION)
    }

    /**
     * Inserts a new card at the given position using proper insertion animation
     * (the card scroller will animate to the new card).
     */
    private fun insertNewCard(position: Int) {
        // Insert new card in the adapter, but don't call notifyDataSetChanged()
        // yet. Instead, request proper animation to inserted card from card scroller,
        // which will notify the adapter at the right time during the animation.
        val card = CardBuilder(this, CardBuilder.Layout.COLUMNS)
        val action =
            Action.values()[mRandom.nextInt(8)]
        card.setText(action.textId).addImage(action.imageId)
        (mAdapter as? CardAdapterWithMutations)?.insertCardWithoutNotification(position, card, action)
        mCardScroller?.animate(position, CardScrollView.Animation.INSERTION)
    }
}