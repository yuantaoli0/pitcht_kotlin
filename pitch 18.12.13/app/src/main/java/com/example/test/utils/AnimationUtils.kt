package com.example.test.utils

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation


object AnimationUtils {
    fun fadeIn(view: View,time:Long){
        if (view.getVisibility() === View.VISIBLE) {
            return
        }
        val animation: Animation = AlphaAnimation(0f, 1f)
        animation.duration = time
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                view.setEnabled(true)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        view.startAnimation(animation)
        view.setVisibility(View.VISIBLE)
    }

    fun fadeIn(view: View) {
        fadeIn(view,1000)
    }

    fun fadeOut(view: View,time:Long){
        if (view.getVisibility() !== View.VISIBLE) {
            return
        }

        // Since the button is still clickable before fade-out animation
        // ends, we disable the button first to block click.
        view.setEnabled(false)
        val animation: Animation = AlphaAnimation(1f, 0f)
        animation.duration = time
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                view.setVisibility(View.GONE)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        view.startAnimation(animation)
    }

    fun fadeOut(view: View) {
        fadeOut(view,1000L)
    }
}