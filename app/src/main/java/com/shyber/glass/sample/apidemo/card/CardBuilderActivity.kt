package com.shyber.glass.sample.apidemo.card

import android.content.Context
import com.google.android.glass.widget.CardBuilder
import com.shyber.glass.sample.apidemo.R
import com.shyber.glass.sample.apidemo.base.BaseCardScrollActivity

/**
 * Creates a card scroll view with examples of different image layout cards.
 */
class CardBuilderActivity : BaseCardScrollActivity() {

    /**
     * Creates list of cards that showcase different type of [CardBuilder] API.
     */
    override fun createCards(context: Context): ArrayList<CardBuilder> {
        val cards = ArrayList<CardBuilder>()

        // Add cards that demonstrate TEXT layouts.
        cards.add(
            CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_card_text_not_fixed)
                .setFootnote(R.string.text_card_footnote)
                .setTimestamp(R.string.text_card_timestamp)
                .setAttributionIcon(R.drawable.ic_smile)
        )
        cards.add(
            createCardWithImages(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_card_text_with_images)
                .setFootnote(R.string.text_card_footnote)
                .setTimestamp(R.string.text_card_timestamp)
                .setAttributionIcon(R.drawable.ic_smile)
        )
        cards.add(
            CardBuilder(context, CardBuilder.Layout.TEXT_FIXED)
                .setText(R.string.text_card_text_fixed)
                .setFootnote(R.string.text_card_footnote)
                .setTimestamp(R.string.text_card_timestamp)
                .setAttributionIcon(R.drawable.ic_smile)
        )
        cards.add(
            CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_card_text_stack_indicator)
                .showStackIndicator(true)
                .setAttributionIcon(R.drawable.ic_smile)
        )

        // Add cards that demonstrate COLUMNS layouts.
        cards.add(
            createCardWithImages(context, CardBuilder.Layout.COLUMNS)
                .setText(R.string.text_card_columns_not_fixed)
                .setFootnote(R.string.text_card_footnote)
                .setTimestamp(R.string.text_card_timestamp)
                .setAttributionIcon(R.drawable.ic_smile)
        )
        cards.add(
            CardBuilder(context, CardBuilder.Layout.COLUMNS)
                .setText(R.string.text_card_columns_with_icon)
                .setIcon(R.drawable.ic_wifi_150)
                .setFootnote(R.string.text_card_footnote)
                .setTimestamp(R.string.text_card_timestamp)
                .setAttributionIcon(R.drawable.ic_smile)
        )
        cards.add(
            createCardWithImages(context, CardBuilder.Layout.COLUMNS)
                .setText(R.string.text_card_columns_fixed)
                .setFootnote(R.string.text_card_footnote)
                .setTimestamp(R.string.text_card_timestamp)
                .setAttributionIcon(R.drawable.ic_smile)
        )

        // Add cards that demonstrate CAPTION layouts.
        cards.add(
            CardBuilder(context, CardBuilder.Layout.CAPTION)
                .addImage(R.drawable.beach)
                .setText(R.string.text_card_caption)
                .setFootnote(R.string.text_card_footnote)
                .setTimestamp(R.string.text_card_timestamp)
                .setAttributionIcon(R.drawable.ic_smile)
        )
        cards.add(
            CardBuilder(context, CardBuilder.Layout.CAPTION)
                .addImage(R.drawable.beach)
                .setText(R.string.text_card_caption_with_icon)
                .setIcon(R.drawable.ic_avatar_70)
                .setFootnote(R.string.text_card_footnote)
                .setTimestamp(R.string.text_card_timestamp)
                .setAttributionIcon(R.drawable.ic_smile)
        )

        // Add cards that demonstrate TITLE layouts.
        cards.add(
            CardBuilder(context, CardBuilder.Layout.TITLE)
                .addImage(R.drawable.beach)
                .setText(R.string.text_card_title)
        )
        cards.add(
            CardBuilder(context, CardBuilder.Layout.TITLE)
                .addImage(R.drawable.beach)
                .setText(R.string.text_card_title_icon)
                .setIcon(R.drawable.ic_phone_50)
        )

        // Add cards that demonstrate MENU layouts.
        cards.add(
            CardBuilder(context, CardBuilder.Layout.MENU)
                .setText(R.string.text_card_menu)
                .setFootnote(R.string.text_card_menu_description)
                .setIcon(R.drawable.ic_phone_50)
        )

        // Add cards that demonstrate ALERT layouts.
        cards.add(
            CardBuilder(context, CardBuilder.Layout.ALERT)
                .setText(R.string.text_card_alert)
                .setFootnote(R.string.text_card_alert_description)
                .setIcon(R.drawable.ic_warning_150)
        )

        // Add cards that demonstrate AUTHOR layouts.
        cards.add(
            CardBuilder(context, CardBuilder.Layout.AUTHOR)
                .setText(R.string.text_card_author_text)
                .setIcon(R.drawable.ic_avatar_70)
                .setHeading(R.string.text_card_author_heading)
                .setSubheading(R.string.text_card_author_subheading)
                .setFootnote(R.string.text_card_footnote)
                .setTimestamp(R.string.text_card_timestamp)
                .setAttributionIcon(R.drawable.ic_smile)
        )
        return cards
    }

    companion object {
        /**
         * Returns a new [CardBuilder] with the specified layout and adds five images to it for
         * the mosaic.
         */
        private fun createCardWithImages(
            context: Context,
            layout: CardBuilder.Layout
        ): CardBuilder {
            val card = CardBuilder(context, layout)
            card.addImage(R.drawable.codemonkey1)
            card.addImage(R.drawable.codemonkey2)
            card.addImage(R.drawable.codemonkey3)
            card.addImage(R.drawable.codemonkey4)
            card.addImage(R.drawable.codemonkey5)
            card.addImage(R.drawable.codemonkey6)
            card.addImage(R.drawable.codemonkey7)
            card.addImage(R.drawable.codemonkey8)
            return card
        }
    }
}