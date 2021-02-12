package com.example.Evermind

import android.view.animation.Interpolator
import androidx.recyclerview.widget.RecyclerView
import jp.wasabeef.recyclerview.animators.BaseItemAnimator

open class customLandingAnimator : BaseItemAnimator {
    constructor()
    constructor(interpolator: Interpolator) {
        this.interpolator = interpolator
    }

    override fun animateRemoveImpl(holder: RecyclerView.ViewHolder) {
        holder.itemView.animate().apply {
            alpha(0f)
                .scaleX(1.15f)
                .scaleY(1.15f)
            duration = removeDuration
            interpolator = interpolator
            setListener(DefaultRemoveAnimatorListener(holder))
            startDelay = getRemoveDelay(holder)
        }.start()
    }

    override fun preAnimateAddImpl(holder: RecyclerView.ViewHolder) {
        holder.itemView.alpha = 0f
        holder.itemView.scaleX = 1.15f
        holder.itemView.scaleY = 1.15f
    }

    override fun animateAddImpl(holder: RecyclerView.ViewHolder) {
        holder.itemView.animate().apply {
            alpha(1f)
            scaleX(1f)
            scaleY(1f)
            duration = addDuration
            interpolator = interpolator
            setListener(DefaultAddAnimatorListener(holder))
            startDelay = getAddDelay(holder)
        }.start()
    }
}