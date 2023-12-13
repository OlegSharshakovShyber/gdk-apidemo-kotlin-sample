package com.shyber.glass.sample.apidemo.touchpad

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import com.google.android.glass.touchpad.GestureDetector
import com.google.android.glass.touchpad.GestureDetector.OneFingerScrollListener
import com.google.android.glass.touchpad.GestureDetector.TwoFingerScrollListener
import com.shyber.glass.sample.apidemo.R

/**
 * Displays information about the continuous gestures reported by the gesture detector (i.e.,
 * scrolling events).
 */
class ContinuousGesturesActivity : Activity(), OneFingerScrollListener,
    TwoFingerScrollListener {
    private var mScrollType: TextView? = null
    private var mDisplacement: TextView? = null
    private var mDelta: TextView? = null
    private var mVelocity: TextView? = null
    private var mGestureDetector: GestureDetector? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_continuous_gestures)
        mScrollType = findViewById(R.id.scroll_type) as TextView
        mDisplacement = findViewById(R.id.displacement) as TextView
        mDelta = findViewById(R.id.delta) as TextView
        mVelocity = findViewById(R.id.velocity) as TextView

        // Initialize the gesture detector and set the activity to listen to the continuous
        // gestures.
        mGestureDetector = GestureDetector(this)
            .setOneFingerScrollListener(this).setTwoFingerScrollListener(this)
    }

    /**
     * Overridden to allow the gesture detector to process motion events that occur anywhere within
     * the activity.
     */
    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        return mGestureDetector?.onMotionEvent(event) ?: super.onGenericMotionEvent(event)
    }

    override fun onOneFingerScroll(
        displacement: Float,
        delta: Float,
        velocity: Float
    ): Boolean {
        mScrollType?.setText(R.string.scroll_one_finger)
        updateScrollInfo(displacement, delta, velocity)
        return true
    }

    override fun onTwoFingerScroll(
        displacement: Float,
        delta: Float,
        velocity: Float
    ): Boolean {
        mScrollType?.setText(R.string.scroll_two_finger)
        updateScrollInfo(displacement, delta, velocity)
        return true
    }

    /**
     * Updates the text views that show the detailed scroll information.
     *
     * @param displacement the scroll displacement (position relative to the original touch-down
     * event)
     * @param delta the scroll delta from the previous touch event
     * @param velocity the velocity of the scroll event
     */
    private fun updateScrollInfo(
        displacement: Float,
        delta: Float,
        velocity: Float
    ) {
        mDisplacement?.text = resources.getString(
            R.string.pixel_distance_units, displacement
        )
        mDelta?.text = resources.getString(R.string.pixel_distance_units, delta)
        mVelocity?.text = resources.getString(R.string.pixel_velocity_units, velocity)
    }
}