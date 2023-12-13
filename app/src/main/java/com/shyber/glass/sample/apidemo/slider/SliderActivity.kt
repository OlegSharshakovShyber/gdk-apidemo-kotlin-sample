package com.shyber.glass.sample.apidemo.slider

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.view.WindowManager
import android.widget.AdapterView.OnItemClickListener
import com.google.android.glass.media.Sounds
import com.google.android.glass.widget.CardBuilder
import com.google.android.glass.widget.Slider
import com.google.android.glass.widget.Slider.GracePeriod
import com.google.android.glass.widget.Slider.Indeterminate
import com.shyber.glass.sample.apidemo.R
import com.shyber.glass.sample.apidemo.base.BaseCardScrollActivity
import kotlin.collections.ArrayList

/**
 * An activity that demonstrates the slider API.
 */
class SliderActivity : BaseCardScrollActivity() {
    private val mGracePeriodListener: GracePeriod.Listener = object : GracePeriod.Listener {
        override fun onGracePeriodEnd() {
            // Play a SUCCESS sound to indicate the end of the grace period.
            val am =
                getSystemService(Context.AUDIO_SERVICE) as AudioManager
            am.playSoundEffect(Sounds.SUCCESS)
            mGracePeriod = null
        }

        override fun onGracePeriodCancel() {
            // Play a DIMISS sound to indicate the cancellation of the grace period.
            val am =
                getSystemService(Context.AUDIO_SERVICE) as AudioManager
            am.playSoundEffect(Sounds.DISMISSED)
            mGracePeriod = null
        }
    }

    private var mSlider: Slider? = null
    private var mIndeterminate: Indeterminate? = null
    private var mGracePeriod: GracePeriod? = null
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        // Ensure screen stays on during demo.
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        mCardScroller?.onItemClickListener =
            OnItemClickListener { _, _, position, _ -> // Plays sound.
                val am =
                    getSystemService(Context.AUDIO_SERVICE) as AudioManager
                am.playSoundEffect(Sounds.TAP)
                processSliderRequest(position)
            }
        setContentView(mCardScroller)
        mSlider = Slider.from(mCardScroller)
    }

    override fun onBackPressed() {
        // If the Grace Period is running, cancel it instead of finishing the Activity.
        if (mGracePeriod != null) {
            mGracePeriod?.cancel()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Processes a request to show a slider.
     *
     * Starting a new Slider, regardless of its type, automatically hides any shown Slider.
     */
    private fun processSliderRequest(position: Int) {
        when (position) {
            SCROLLER -> {
                val scroller =
                    mSlider?.startScroller(MAX_SLIDER_VALUE, 0f)

                // Start an animation showing the different positions of the slider, the slider
                // automatically hides after a short time of inactivity.
                ObjectAnimator.ofFloat(
                    scroller,
                    "position",
                    0f,
                    MAX_SLIDER_VALUE.toFloat()
                )
                    .setDuration(ANIMATION_DURATION_MILLIS)
                    .start()
            }
            DETERMINATE -> {
                val determinate =
                    mSlider?.startDeterminate(MAX_SLIDER_VALUE, 0f)
                val animator = ObjectAnimator.ofFloat(
                    determinate, "position", 0f,
                    MAX_SLIDER_VALUE.toFloat()
                )

                // Hide the slider when the animation stops.
                animator.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        determinate?.hide()
                    }
                })
                // Start an animation showing the different positions of the slider.
                animator.setDuration(ANIMATION_DURATION_MILLIS)
                    .start()
            }
            GRACE_PERIOD ->                 // Start the grace period slider and play a sound when one of the listener method
                // gets fired.
                mGracePeriod = mSlider?.startGracePeriod(mGracePeriodListener)
            INDETERMINATE ->                 // Toggle between showing/hiding the indeterminate slider.
                mIndeterminate = if (mIndeterminate != null) {
                    mIndeterminate?.hide()
                    null
                } else {
                    mSlider?.startIndeterminate()
                }
        }
    }

    /**
     * Create a list of cards to display as activity content.
     */
    override fun createCards(context: Context): ArrayList<CardBuilder> {
        val cards = ArrayList<CardBuilder>()
        cards.add(
            SCROLLER, CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_slider_scroller)
        )
        cards.add(
            DETERMINATE, CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_slider_determinate)
        )
        cards.add(
            GRACE_PERIOD, CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_slider_grace_period)
        )
        cards.add(
            INDETERMINATE, CardBuilder(context, CardBuilder.Layout.TEXT)
                .setText(R.string.text_slider_indeterminate)
        )
        return cards
    }

    companion object {
        // Index of slider demo cards.
        private const val SCROLLER = 0
        private const val DETERMINATE = 1
        private const val GRACE_PERIOD = 2
        private const val INDETERMINATE = 3
        private const val MAX_SLIDER_VALUE = 5
        private const val ANIMATION_DURATION_MILLIS: Long = 5000
    }
}