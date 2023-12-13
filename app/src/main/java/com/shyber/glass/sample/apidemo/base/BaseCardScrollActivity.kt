package com.shyber.glass.sample.apidemo.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.android.glass.widget.CardBuilder
import com.google.android.glass.widget.CardScrollAdapter
import com.google.android.glass.widget.CardScrollView
import com.shyber.glass.sample.apidemo.card.CardAdapter

abstract class BaseCardScrollActivity  : Activity() {
    protected open var mCardScroller: CardScrollView? = null
    protected open var mAdapter: CardScrollAdapter? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        mCardScroller = CardScrollView(this)
        mAdapter = CardAdapter(createCards(this))
        mCardScroller?.adapter = mAdapter
        setContentView(mCardScroller)
    }

    protected open fun createCards(context: Context): ArrayList<CardBuilder> {
        return ArrayList()
    }

    override fun onResume() {
        super.onResume()
        mCardScroller?.activate()
    }

    override fun onPause() {
        mCardScroller?.deactivate()
        super.onPause()
    }

}