package com.shyber.glass.sample.apidemo.touchpad

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.InputDevice
import android.view.MotionEvent
import android.widget.RelativeLayout
import android.widget.TextView
import com.shyber.glass.sample.apidemo.R

/**
 * Provides a visual representation of the Glass touchpad, with colored and labeled circles that
 * represent the locations of the user's fingers when they are on the touchpad.
 */
class TouchpadView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    // The views used to display the location of a finger on the touchpad.
    private val mFingerTraceViews =
        arrayOfNulls<TextView>(FINGER_RES_IDS.size)

    // The horizontal and vertical hardware resolutions of the touchpad. These are used to
    // calculate the aspect ratio of the view when it is measured.
    private var mTouchpadHardwareWidth = 0f
    private var mTouchpadHardwareHeight = 0f
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Constrains the view's dimensions to have the same aspect ratio as the actual hardware
        // touchpad.
        val newHeight = (MeasureSpec.getSize(widthMeasureSpec) / mTouchpadHardwareWidth *
                mTouchpadHardwareHeight).toInt()
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(newHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }

    /**
     * Processes all the pointers that are part of the motion event and displays the finger traces
     * at the proper positions in the view.
     *
     *
     * Since this view is only intended to render motion events and not consume them, we always
     * return false so that the events bubble up to the activity and the gesture detector has a
     * chance to handle them.
     */
    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        logVerbose(printToString(event))
        for (i in 0 until event.pointerCount) {
            val pointerId = event.getPointerId(i)
            val x = event.getX(i)
            val y = event.getY(i)
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_MOVE -> moveFingerTrace(
                    pointerId,
                    x,
                    y
                )
                MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> hideFingerTrace(
                    pointerId
                )
            }
        }
        return false
    }

    /** Looks up the hardware resolution of the Glass touchpad.  */
    private fun lookupTouchpadHardwareResolution() {
        val deviceIds = InputDevice.getDeviceIds()
        for (deviceId in deviceIds) {
            val device = InputDevice.getDevice(deviceId)
            if (device.sources and InputDevice.SOURCE_TOUCHPAD != 0) {
                logVerbose(
                    "Touchpad motion range: x-axis [%d, %d] y-axis [%d, %d]",
                    device.getMotionRange(MotionEvent.AXIS_X).min,
                    device.getMotionRange(MotionEvent.AXIS_X).max,
                    device.getMotionRange(MotionEvent.AXIS_Y).min,
                    device.getMotionRange(MotionEvent.AXIS_Y).max
                )
                mTouchpadHardwareWidth = device.getMotionRange(MotionEvent.AXIS_X).range
                mTouchpadHardwareHeight = device.getMotionRange(MotionEvent.AXIS_Y).range
                // Stop after we've seen the first touchpad device, because there might be multiple
                // devices in this list if the user is currently screencasting with MyGlass. The
                // first one will always be the hardware touchpad.
                break
            }
        }
    }

    /**
     * Creates a new view that will be used to display the specified pointer id on the touchpad
     * view.
     *
     * @param pointerId the id of the pointer that this finger trace view will represent; used to
     * determine its color and text
     * @return the `TextView` that was created
     */
    private fun createFingerTraceView(pointerId: Int): TextView {
        val fingerTraceView = TextView(context)
        fingerTraceView.setBackgroundResource(FINGER_RES_IDS[pointerId])
        fingerTraceView.text = Integer.toString(pointerId)
        fingerTraceView.gravity = Gravity.CENTER
        fingerTraceView.setTextAppearance(
            context,
            android.R.style.TextAppearance_DeviceDefault_Small
        )
        fingerTraceView.alpha = 0f
        val lp = LayoutParams(
            FINGER_TRACE_SIZE, FINGER_TRACE_SIZE
        )
        lp.addRule(ALIGN_PARENT_TOP)
        lp.addRule(ALIGN_PARENT_LEFT)
        // The right and bottom margin here are required so that the view doesn't get "squished"
        // as it touches the right or bottom side of the touchpad view.
        lp.rightMargin = -2 * FINGER_TRACE_SIZE
        lp.bottomMargin = -2 * FINGER_TRACE_SIZE
        fingerTraceView.layoutParams = lp
        return fingerTraceView
    }

    /**
     * Moves the finger trace associated with the specified pointer id to a new location in the
     * view.
     *
     * @param pointerId the pointer id of the finger trace to move
     * @param point the new location of the finger trace
     */
    private fun moveFingerTrace(pointerId: Int, x: Float, y: Float) {
        val fingerTraceView = mFingerTraceViews[pointerId]

        // Cancel any current animations on the view and bring it back to full opacity.
        fingerTraceView?.animate()?.cancel()
        fingerTraceView?.scaleX = 1.0f
        fingerTraceView?.scaleY = 1.0f
        fingerTraceView?.alpha = 1f

        // Reposition the finger trace by updating the layout margins of its view.
        val lp =
            fingerTraceView?.layoutParams as? LayoutParams
        val viewX = (x / mTouchpadHardwareWidth * width).toInt()
        val viewY = (y / mTouchpadHardwareHeight * height).toInt()
        lp?.leftMargin = viewX - FINGER_TRACE_SIZE / 2
        lp?.topMargin = viewY - FINGER_TRACE_SIZE / 2
        fingerTraceView?.layoutParams = lp
    }

    /**
     * Hides the finger trace associated with the specified pointer id. Traces are faded away
     * instead of immediately hidden in order to reduce flickering due to intermittence in the
     * touchpad.
     *
     * @param pointerId the pointer id whose finger trace should be hidden
     */
    private fun hideFingerTrace(pointerId: Int) {
        val fingerTraceView = mFingerTraceViews[pointerId]
        fingerTraceView?.animate()
            ?.scaleX(3.0f)
            ?.scaleY(3.0f)
            ?.setDuration(FADE_OUT_DURATION_MILLIS.toLong())?.alpha(0f)
    }

    /**
     * Helper method to print to the main logcat stream.
     *
     *
     * In order to turn the debugging on, execute following command.
     * <pre> $ adb shell setprop log.tag.[TAG] VERBOSE </pre>
     */
    private fun logVerbose(format: String, vararg args: Any) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, String.format(format, *args))
        }
    }

    companion object {
        private val TAG = TouchpadView::class.java.simpleName

        // The size of the finger trace drawn at the location of one of the user's fingers.
        private const val FINGER_TRACE_SIZE = 50

        // The drawable ids used to draw the user's three fingers.
        private val FINGER_RES_IDS = intArrayOf(
            R.drawable.finger_trace_0, R.drawable.finger_trace_1, R.drawable.finger_trace_2
        )

        // The duration, in milliseconds, of the animation used to fade out a finger trace when the
        // user's finger is lifted from the touchpad.
        private const val FADE_OUT_DURATION_MILLIS = 100

        /**
         * Helper method to debug the motion events.
         */
        private fun printToString(ev: MotionEvent): String {
            val historySize = ev.historySize
            val pointerCount = ev.pointerCount
            val resultString = StringBuilder()
            for (h in 0 until historySize) {
                resultString.append(
                    String.format(
                        "At time %d:",
                        ev.getHistoricalEventTime(h)
                    )
                )
                for (p in 0 until pointerCount) {
                    resultString.append(
                        String.format(
                            "  pointer %d %s: (%f, %f)",
                            ev.getPointerId(p), MotionEvent.actionToString(ev.actionMasked),
                            ev.getHistoricalX(p, h), ev.getHistoricalY(p, h)
                        )
                    )
                }
            }
            resultString.append(String.format("At time %d:", ev.eventTime))
            for (p in 0 until pointerCount) {
                resultString.append(
                    String.format(
                        "  pointer %d %s: (%f, %f)",
                        ev.getPointerId(p), MotionEvent.actionToString(ev.actionMasked),
                        ev.getX(p), ev.getY(p)
                    )
                )
            }
            return resultString.toString()
        }
    }

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        clipChildren = false
        lookupTouchpadHardwareResolution()
        for (i in mFingerTraceViews.indices) {
            mFingerTraceViews[i] = createFingerTraceView(i)
            addView(mFingerTraceViews[i])
        }
    }
}