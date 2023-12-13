package com.shyber.glass.sample.apidemo.theming

import android.os.Bundle
import com.shyber.glass.sample.apidemo.base.BaseCardScrollActivity

/**
 * Creates a card scroll view with examples of the default textAppearance styles on Glass.
 */
class TextAppearanceActivity : BaseCardScrollActivity() {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        mAdapter = TextAppearanceAdapter(this)
        mCardScroller?.adapter = mAdapter
    }
}