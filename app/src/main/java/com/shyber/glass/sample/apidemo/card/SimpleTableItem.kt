package com.shyber.glass.sample.apidemo.card

/**
 * Simple data object used to represent items in a table, which are converted into views by
 * [EmbeddedCardLayoutAdapter].
 */
class SimpleTableItem
/**
 * Initializes a new `SimpleTableItem` with the specified icon, primary text, and
 * secondary text.
 */(
    /** The image resource ID associated with the table item.  */
    val iconResId: Int,
    /** The primary text associated with the table item.  */
    val primaryText: CharSequence,
    /** The secondary text associated with the table item.  */
    val secondaryText: CharSequence
)