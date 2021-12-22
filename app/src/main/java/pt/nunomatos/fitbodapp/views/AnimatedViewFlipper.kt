package pt.nunomatos.fitbodapp.views

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.DecelerateInterpolator
import android.widget.ViewFlipper

class AnimatedViewFlipper @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        ViewFlipper(context, attrs) {

    companion object {
        private const val ANIMATION_DURATION = 200L
    }

    init {
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.startOffset = ANIMATION_DURATION
        fadeIn.interpolator = DecelerateInterpolator()
        fadeIn.duration = ANIMATION_DURATION

        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.interpolator = AccelerateDecelerateInterpolator()
        fadeOut.duration = ANIMATION_DURATION

        inAnimation = fadeIn
        outAnimation = fadeOut
    }

}