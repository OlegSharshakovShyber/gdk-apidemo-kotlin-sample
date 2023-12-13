package com.shyber.glass.sample.apidemo.opengl

import android.app.Activity
import android.content.Intent
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import com.shyber.glass.sample.apidemo.R

/**
 * Activity showing an options menu to stop the [OpenGlService].
 */
class MenuActivity : Activity() {
    private val mHandler = Handler()
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        openOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.opengl_livecard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection.
        return when (item.itemId) {
            R.id.stop -> {
                // Stop the service at the end of the message queue for proper options menu
                // animation. This is only needed when starting a new Activity or stopping a Service
                // that published a LiveCard.
                mHandler.post { stopService(Intent(this@MenuActivity, OpenGlService::class.java)) }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onOptionsMenuClosed(menu: Menu) {
        // Nothing else to do, closing the Activity.
        finish()
    }
}