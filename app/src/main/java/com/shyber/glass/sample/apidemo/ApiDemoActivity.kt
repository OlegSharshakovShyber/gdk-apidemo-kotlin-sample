package com.shyber.glass.sample.apidemo

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import com.google.android.glass.media.Sounds
import com.google.android.glass.widget.CardBuilder
import com.shyber.glass.sample.apidemo.base.BaseCardScrollActivity
import com.shyber.glass.sample.apidemo.card.CardBuilderActivity
import com.shyber.glass.sample.apidemo.card.CardScrollViewActivity
import com.shyber.glass.sample.apidemo.card.EmbeddedCardLayoutActivity
import com.shyber.glass.sample.apidemo.opengl.OpenGlService
import com.shyber.glass.sample.apidemo.slider.SliderActivity
import com.shyber.glass.sample.apidemo.theming.TextAppearanceActivity
import com.shyber.glass.sample.apidemo.touchpad.SelectGestureDemoActivity
import com.shyber.glass.sample.apidemo.voicemenu.VoiceMenuActivity
import kotlin.collections.ArrayList

/**
 * Creates a card scroll view with examples of different GDK APIs.
 *
 *
 *  1.  CardBuilder API
 *  1.  CardScrollView API
 *  1.  GestureDetector
 *  1.  textAppearance[Large|Medium|Small]
 *  1.  OpenGL LiveCard
 *  1.  VoiceMenu
 *
 */
class ApiDemoActivity : BaseCardScrollActivity() {

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setCardScrollerListener()
    }

    /**
     * Create list of API demo cards.
     */
    override fun createCards(context: Context): ArrayList<CardBuilder> {
        val cards = ArrayList<CardBuilder>()
        cards.add(
            CARD_BUILDER,
            CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_card_builder)
        )
        cards.add(
            CARD_BUILDER_EMBEDDED_LAYOUT,
            CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_card_builder_embedded_layout)
        )
        cards.add(
            CARD_SCROLL_VIEW,
            CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_card_scroll_view)
        )
        cards.add(
            GESTURE_DETECTOR,
            CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_gesture_detector)
        )
        cards.add(
            TEXT_APPEARANCE,
            CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_text_appearance)
        )
        cards.add(
            OPENGL,
            CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_opengl)
        )
        cards.add(
            VOICE_MENU,
            CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_voice_menu)
        )
        cards.add(
            SLIDER,
            CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_slider)
        )
        return cards
    }

    /**
     * Different type of activities can be shown, when tapped on a card.
     */
    private fun setCardScrollerListener() {
        mCardScroller?.onItemClickListener = OnItemClickListener { _, _, position, id ->
            Log.d(
                TAG,
                "Clicked view at position $position, row-id $id"
            )
            var soundEffect = Sounds.TAP
            when (position) {
                CARD_BUILDER -> startActivity(
                    Intent(this@ApiDemoActivity, CardBuilderActivity::class.java)
                )
                CARD_BUILDER_EMBEDDED_LAYOUT -> startActivity(
                    Intent(
                        this@ApiDemoActivity, EmbeddedCardLayoutActivity::class.java
                    )
                )
                CARD_SCROLL_VIEW -> startActivity(
                    Intent(
                        this@ApiDemoActivity,
                        CardScrollViewActivity::class.java
                    )
                )
                GESTURE_DETECTOR -> startActivity(
                    Intent(
                        this@ApiDemoActivity,
                        SelectGestureDemoActivity::class.java
                    )
                )
                TEXT_APPEARANCE -> startActivity(
                    Intent(
                        this@ApiDemoActivity,
                        TextAppearanceActivity::class.java
                    )
                )
                OPENGL -> startService(
                    Intent(this@ApiDemoActivity, OpenGlService::class.java)
                )
                VOICE_MENU -> startActivity(
                    Intent(this@ApiDemoActivity, VoiceMenuActivity::class.java)
                )
                SLIDER -> startActivity(
                    Intent(this@ApiDemoActivity, SliderActivity::class.java)
                )
                else -> {
                    soundEffect = Sounds.ERROR
                    Log.d(
                        TAG,
                        "Don't show anything"
                    )
                }
            }

            // Play sound.
            val am =
                getSystemService(Context.AUDIO_SERVICE) as AudioManager
            am.playSoundEffect(soundEffect)
        }
    }

    companion object {
        private val TAG =
            ApiDemoActivity::class.java.simpleName

        // Index of api demo cards.
        // Visible for testing.
        const val CARD_BUILDER = 0
        const val CARD_BUILDER_EMBEDDED_LAYOUT = 1
        const val CARD_SCROLL_VIEW = 2
        const val GESTURE_DETECTOR = 3
        const val TEXT_APPEARANCE = 4
        const val OPENGL = 5
        const val VOICE_MENU = 6
        const val SLIDER = 7
    }
}