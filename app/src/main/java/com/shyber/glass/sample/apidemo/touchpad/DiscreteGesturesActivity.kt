package com.shyber.glass.sample.apidemo.touchpad

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import com.google.android.glass.touchpad.Gesture
import com.google.android.glass.touchpad.GestureDetector
import com.google.android.glass.touchpad.GestureDetector.BaseListener
import com.google.android.glass.touchpad.GestureDetector.FingerListener
import com.shyber.glass.sample.apidemo.R

/**
 * Displays information about the discrete gestures reported by the gesture detector (i.e., basic
 * tap/swipe gestures and finger counts).
 */
class DiscreteGesturesActivity : Activity(), BaseListener, FingerListener {
    private var mLastGesture: TextView? = null
    private var mFingerCount: TextView? = null
    private var mSwipeAgainTip: TextView? = null
    private var mGestureDetector: GestureDetector? = null
    private var mSwipedDownOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discrete_gestures)
        mLastGesture = findViewById(R.id.last_gesture) as TextView
        mFingerCount = findViewById(R.id.finger_count) as TextView
        mSwipeAgainTip = findViewById(R.id.swipe_again_tip) as TextView

        // Initialize the gesture detector and set the activity to listen to discrete gestures.
        mGestureDetector =
            GestureDetector(this).setBaseListener(this)
                .setFingerListener(this)
    }

    /**
     * Overridden to allow the gesture detector to process motion events that occur anywhere within
     * the activity.
     */
    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        return mGestureDetector?.onMotionEvent(event) ?: super.onGenericMotionEvent(event)
    }

    /**
     * This method includes special behavior to handle SWIPE_DOWN gestures. The first time the user
     * swipes down, we return true so that the user can still see the feedback in the gesture
     * label, and we fade in an instructional tip label. The second time the user swipes down, we
     * return false so that the activity can handle the event and return to the previous activity.
     */
    override fun onGesture(gesture: Gesture): Boolean {
        mLastGesture?.text = gesture.name
        return if (gesture == Gesture.SWIPE_DOWN) {
            if (!mSwipedDownOnce) {
                mSwipeAgainTip?.animate()?.alpha(1.0f)
                mSwipedDownOnce = true
                true
            } else {
                false
            }
        } else {
            true
        }
    }

    override fun onFingerCountChanged(previousCount: Int, currentCount: Int) {
        mFingerCount?.text = Integer.toString(currentCount)
    }
}