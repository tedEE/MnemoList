package ru.jeinmentalist.mail.mnemolist.utils

import android.animation.*
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.DecelerateInterpolator
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import ru.jeinmentalist.mail.mentalist.R
import kotlin.math.hypot


/**
 * Кастыль
 * без этих смещенний не получаеться поймать центр fab не знаю почему
 */
const val OFFSET_CENTER_X_y = 100
const val OFFSET_FAB_MENU_FRAGMENT = 160

/** Запускает круговую анимацию раскрытия
 * [fromLeft] если `true`, тогда анимация начинается с
 * нижнего левого угла [View], иначе начинается с нижнего правого */

fun View.startCircularReveal(x: Int, y: Int, animator: ObjectAnimator?) {
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            v: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            v.removeOnLayoutChangeListener(this)
//            val centerX = v.right - OFFSET_CENTER_X_y
//            val centerY = v.bottom - OFFSET_CENTER_X_y
            val centerX = x
            val centerY = y
            val radius = hypot(right.toDouble(), bottom.toDouble()).toInt()
            val durationAnim: Long = 1000
            ViewAnimationUtils.createCircularReveal(v, centerX, centerY, 0F, radius.toFloat())
                .apply {
                    interpolator = DecelerateInterpolator(2f)
                    duration = durationAnim
                    start()
                    animator?.start()
                }
        }

    })
}

/** * Анимировать выход из фрагмента, используя заданные параметры в качестве конечной точки анимации.
 *  Запускает заданный блок кода после завершения анимации.
 *  @param exitX: Координата X конечной точки анимации.
 * @param exitY: Координата Y конечной точки анимации.
 * @param block: Блок кода, который будет выполняться после завершения анимации. */
fun View.exitCircularReveal(exitX: Int, exitY: Int, animator: ObjectAnimator? = null, block: () -> Unit) {
    val startRadius = hypot(this.width.toDouble(), this.height.toDouble())
    ViewAnimationUtils.createCircularReveal(this, exitX, exitY, startRadius.toFloat(), 0f).apply {
        duration = 500
        interpolator = DecelerateInterpolator(1f)
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                block()
                animator?.start()
                super.onAnimationEnd(animation)
            }
        })
        start()
    }
}

/** @return положение центра текущего [View] на экране */

fun View.findLocationOfCenterOnTheScreen(): IntArray {
    val positions = intArrayOf(0, 0)
//    getLocationOnScreen(positions)
    // Get the center of the view
//    positions[0] = positions[0] + width / 2
//    positions[1] = positions[1] + height / 2
    positions[0] = (x + width / 2).toInt()
    positions[1] = (y + height / 2).toInt()
    return positions
}

fun View.exitReveal(block: () -> Unit) {
    // get the center for the clipping circle
    val cx = this.measuredWidth / 2
    val cy = this.measuredHeight / 2

    // get the initial radius for the clipping circle
    val initialRadius = this.width / 2

    // create the animation (the final radius is zero)
    val anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, initialRadius.toFloat(), 0f)

    // make the view invisible when the animation is done
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            this@exitReveal.visibility = View.INVISIBLE
            block()
        }
    })

    // start the animation
    anim.start()
}

fun View.enterReveal(block: (View) -> Unit = {}) {

    // get the center for the clipping circle
    val cx = this.measuredWidth / 2
    val cy = this.measuredHeight / 2

    // get the final radius for the clipping circle
    val finalRadius = Math.max(this.width, this.height) / 2
    val anim = ViewAnimationUtils.createCircularReveal(this, cx, cy, 0f, finalRadius.toFloat())
    this.visibility = View.VISIBLE
    anim.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {}
        override fun onAnimationEnd(animation: Animator) {
//            getWindow().getEnterTransition().removeListener(mEnterTransitionListener)
            block.invoke(this@enterReveal)
        }

        override fun onAnimationCancel(animation: Animator) {}
        override fun onAnimationRepeat(animation: Animator) {}
    })
    anim.start()
}

fun View.startBackgroundColorAnimation(
    startColor: Int,
    endColor: Int,
    duration: Long
): ObjectAnimator {
    return ObjectAnimator.ofObject(
        this, "backgroundColor",
        ArgbEvaluator(),
        startColor,
        endColor
    ).apply {
        this.duration = duration
    }
}

/** * Должен быть реализован фрагментами, которые должны выходить с круговым раскрытием
 * анимация вместе с [isToBeExitedWithAnimation] возвращает true
 * @property posX положение оси X центра круглого раскрытия
 * @property posY положение оси Y центра круглого раскрытия */

interface ExitWithAnimation {
    var posX: Int?
    var posY: Int?

    fun close(callback: ()->Unit = {})

    fun checkStatus(): Boolean {
        if ((this as? ExitWithAnimation)?.isToBeExitedWithAnimation() == true) {
            if (this.posX != null || this.posY != null) {
                return true
            }
        }
        return false
    }

/**
 * Must return true if required to exit with circular reveal animation
 */
fun isToBeExitedWithAnimation(): Boolean
}
