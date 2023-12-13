package com.shyber.glass.sample.apidemo.opengl

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.android.glass.timeline.LiveCard
import com.google.android.glass.timeline.LiveCard.PublishMode

/**
 * Creates a [LiveCard] rendering a rotating 3D cube with OpenGL.
 */
class OpenGlService : Service() {
    private var mLiveCard: LiveCard? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (mLiveCard == null) {
            mLiveCard = LiveCard(this, LIVE_CARD_TAG)
            mLiveCard?.setRenderer(CubeRenderer())
            mLiveCard?.setAction(
                PendingIntent.getActivity(this, 0, Intent(this, MenuActivity::class.java), 0)
            )
            mLiveCard?.attach(this)
            mLiveCard?.publish(PublishMode.REVEAL)
        } else {
            mLiveCard?.navigate()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        mLiveCard?.isPublished?.let { isPublished ->
            if (isPublished) {
                mLiveCard?.unpublish()
                mLiveCard = null
            }
        }
        super.onDestroy()
    }

    companion object {
        private const val LIVE_CARD_TAG = "opengl"
    }
}