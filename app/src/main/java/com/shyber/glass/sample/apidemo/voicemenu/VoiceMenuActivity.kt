package com.shyber.glass.sample.apidemo.voicemenu

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView.OnItemClickListener
import com.google.android.glass.media.Sounds
import com.google.android.glass.view.WindowUtils
import com.google.android.glass.widget.CardBuilder
import com.shyber.glass.sample.apidemo.R
import com.shyber.glass.sample.apidemo.base.BaseCardScrollActivity
import com.shyber.glass.sample.apidemo.card.CardAdapter
import java.util.*

/**
 * An activity that demonstrates the voice menu API.
 */
class VoiceMenuActivity : BaseCardScrollActivity() {
    private var mPicture = 0
    private var mVoiceMenuEnabled = true
    override fun onCreate(bundle: Bundle?) {

        // Requests a voice menu on this activity. As for any other window feature,
        // be sure to request this before setContentView() is called
        window.requestFeature(WindowUtils.FEATURE_VOICE_COMMANDS)

        // Ensure screen stays on during demo.
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        super.onCreate(bundle)

        // Sets up a singleton card scroller as content of this activity. Clicking
        // on the card toggles the voice menu on and off.
        mCardScroller?.onItemClickListener =
            OnItemClickListener { _, _, _, _ -> // Plays sound.
                val am =
                    getSystemService(Context.AUDIO_SERVICE) as AudioManager
                am.playSoundEffect(Sounds.TAP)
                // Toggles voice menu. Invalidates menu to flag change.
                mVoiceMenuEnabled = !mVoiceMenuEnabled
                window.invalidatePanelMenu(WindowUtils.FEATURE_VOICE_COMMANDS)
            }
    }

    override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            menuInflater.inflate(R.menu.voice_menu, menu)
            return true
        }
        // Good practice to pass through, for options menu.
        return super.onCreatePanelMenu(featureId, menu)
    }

    override fun onPreparePanel(featureId: Int, view: View?, menu: Menu?): Boolean {
        return if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            // Dynamically decides between enabling/disabling voice menu.
            mVoiceMenuEnabled
        } else super.onPreparePanel(featureId, view, menu)
        // Good practice to pass through, for options menu.
    }

    override fun onMenuItemSelected(
        featureId: Int,
        item: MenuItem
    ): Boolean {
        if (featureId == WindowUtils.FEATURE_VOICE_COMMANDS) {
            mPicture = when (item.itemId) {
                R.id.menu_designer -> 0
                R.id.menu_coder1 -> 1
                R.id.menu_coder2 -> 2
                R.id.menu_coder3 -> 3
                R.id.menu_coder4 -> 4
                R.id.menu_coder5 -> 5
                R.id.menu_product -> 6
                else -> return true // No change.
            }
            mCardScroller?.adapter = CardAdapter(createCards(this))
            return true
        }
        return super.onMenuItemSelected(featureId, item)
    }

    /**
     * Creates a singleton card list to display as activity content.
     */
    override fun createCards(context: Context): ArrayList<CardBuilder> {
        val cards = ArrayList<CardBuilder>()
        val card = CardBuilder(context, CardBuilder.Layout.TEXT)
            .addImage(imageResource)
            .setText(R.string.voice_menu_explanation)
        cards.add(card)
        return cards
    }

    /** Returns current image resource.  */
    private val imageResource: Int
        get() = when (mPicture) {
            1 -> R.drawable.codemonkey1
            2 -> R.drawable.codemonkey2
            3 -> R.drawable.codemonkey3
            4 -> R.drawable.codemonkey4
            5 -> R.drawable.codemonkey5
            6 -> R.drawable.product
            else -> R.drawable.designer
        }
}