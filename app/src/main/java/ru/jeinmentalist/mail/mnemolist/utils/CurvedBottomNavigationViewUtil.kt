package ru.jeinmentalist.mail.mnemolist.utils

import android.view.MotionEvent
import android.view.View
import np.com.susanthapa.curved_bottom_navigation.CurvedBottomNavigationView
import java.lang.reflect.Field

fun CurvedBottomNavigationView.getPrivateField(fieldName: String): Any? {
    return try {
        val field: Field = CurvedBottomNavigationView::class.java.getDeclaredField(fieldName)
        field.isAccessible = true
        field.get(this)
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
        null
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
        null
    }
}

fun CurvedBottomNavigationView.setFabTouchListener(onTouch: (View, MotionEvent) -> Boolean) {
    setOnTouchListener { view, event ->
        val x = event.x
        val y = event.y

        val centerX = getPrivateField("centerX") as? Float ?: return@setOnTouchListener false
        val curCenterY = getPrivateField("curCenterY") as? Float ?: return@setOnTouchListener false
        val fabRadius = getPrivateField("fabRadius") as? Float ?: return@setOnTouchListener false

        val isInsideFab = (Math.pow((x - centerX).toDouble(), 2.0) + Math.pow((y - curCenterY).toDouble(), 2.0) <= Math.pow(fabRadius.toDouble(), 2.0))

        if (isInsideFab) {
            if (event.action == MotionEvent.ACTION_UP) {
                view.performClick()
            }
            onTouch(this, event)
        } else {
            false
        }
    }
}
