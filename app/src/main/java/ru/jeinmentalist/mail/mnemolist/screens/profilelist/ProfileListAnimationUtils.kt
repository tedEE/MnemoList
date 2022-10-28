package ru.jeinmentalist.mail.mnemolist.screens.profilelist
//
//import android.animation.ObjectAnimator
//import android.graphics.drawable.AnimationDrawable
//import android.view.View
//import android.view.ViewPropertyAnimator
//import android.view.animation.AnimationUtils
//import androidx.core.animation.addListener
//import ru.jeinmentalist.mail.mentalist.R
//
//// метод исправляет баг с подергиванием анимации иконки fab
//fun ProfileListFragment.dischargeFab() {
//    hideFabTitle(0, 0F, 0F)
//    // ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_add, null) надо разобраться как использовать
////    binding.fab.setImageDrawable(resources.getDrawable(R.drawable.fab_animate, null))
//}
//
//fun ProfileListFragment.hideFabTitle(duration: Long, from: Float, to: Float) {
//    animationFabTitle(binding.fabMenuTitle1, duration, from, to)
//    animationFabTitle(binding.fabMenuTitle2, duration, from, to)
//    animationFabTitle(binding.fabMenuTitle3, duration, from, to)
//
//    binding.fab.setImageDrawable(resources.getDrawable(R.drawable.fab_animate_reverse, null))
//    val sourceAnimationDrawable: AnimationDrawable = (binding.fab.drawable as AnimationDrawable)
//    sourceAnimationDrawable.start()
//}
//
//fun ProfileListFragment.animationFabButton(view: View, duration: Long) {
//    val animation_fab_menu_button =
//        AnimationUtils.loadAnimation(context, R.anim.fab_menu_button)
//    animation_fab_menu_button.duration = duration
//    view.startAnimation(animation_fab_menu_button)
//}
//
//private fun animationFabTitle(view: View, duration: Long, from: Float, to: Float) {
//    val animator: ViewPropertyAnimator = view.animate()
//    animator.alphaBy(from)
//        .alpha(to)
//        .duration = duration
//    animator.start()
//}
//
//private fun animationFabItem(
//    view: View,
//    duration: Long, toY: Float,
//    callbackStart: () -> Unit = {},
//    callbackEnd: () -> Unit = {}
//) {
//    val animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, toY)
//    animator.duration = duration
////        animator.interpolator
//    animator.addListener(
//        onStart = { callbackStart() },
//        onEnd = { callbackEnd() }
//    )
//    animator.start()
//}
//
//fun ProfileListFragment.closeFabMenu(duration: Long) {
//    isFabMenuOpen = false
//    hideFabTitle(duration, 1F, 0F)
//    animationFabItem(
//        binding.fabMenuItem1,
//        duration,
//        0F,
//        callbackEnd = { binding.fabMenuItem1.visibility = View.GONE }
//    )
//    animationFabItem(
//        binding.fabMenuItem2,
//        duration,
//        0F,
//        callbackEnd = { binding.fabMenuItem2.visibility = View.GONE }
//    )
//    animationFabItem(
//        binding.fabMenuItem3,
//        duration,
//        0F,
//        callbackEnd = { binding.fabMenuItem2.visibility = View.GONE }
//    )
//}
//
//fun ProfileListFragment.showFabMenu(duration: Long) {
//    isFabMenuOpen = true
//
////    animationFabItem(
////        binding.fabMenuItem1,
////        duration,
////        -resources.getDimension(R.dimen.standard_55),
////        callbackStart = { binding.fabMenuItem1.visibility = View.VISIBLE }
////    )
////    animationFabItem(
////        binding.fabMenuItem2,
////        duration,
////        -resources.getDimension(R.dimen.standard_105),
////        callbackStart = { binding.fabMenuItem2.visibility = View.VISIBLE }
////    )
////    animationFabItem(
////        binding.fabMenuItem3,
////        duration,
////        -resources.getDimension(R.dimen.standard_155),
////        callbackEnd = { binding.fab.isClickable = true },
////        callbackStart = {
////            binding.fab.isClickable = false
////            binding.fabMenuItem3.visibility = View.VISIBLE
////        }
////    )
//
//    animationFabTitle(binding.fabMenuTitle1, duration, 0F, 1F)
//    animationFabTitle(binding.fabMenuTitle2, duration, 0F, 1F)
//    animationFabTitle(binding.fabMenuTitle3, duration, 0F, 1F)
//
//    animationFabButton(binding.fab, duration)
//    animationFabButton(binding.fab2, duration)
//    animationFabButton(binding.fab3, duration)
//
//
//    binding.fab.setImageDrawable(resources.getDrawable(R.drawable.fab_animate, null))
//    val sourceAnimationDrawable: AnimationDrawable = (binding.fab.drawable as AnimationDrawable)
//    sourceAnimationDrawable.start()
//}
//
//fun ProfileListFragment.hideFabItem() {
//    binding.fabMenuItem1.visibility = View.GONE
//    binding.fabMenuItem2.visibility = View.GONE
//    binding.fabMenuItem3.visibility = View.GONE
//}