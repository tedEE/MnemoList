package ru.jeinmentalist.mail.mnemolist.contract

import android.os.Parcelable
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

typealias ResultListener<T> = (T)->Unit

fun Fragment.navigator(): Navigator{
    return requireActivity() as Navigator
}

interface Navigator {
    // метод можно будет переименовать вроде goToMenu()
    fun goToFirstFragment()
    // показ экранов
    fun showListNote()
    fun showCreateProfileFragment(options: Options)
    fun showCreateNoteFragment(options: Options)
    fun showListNoteFragment(options: Options)
    fun goBack()
    fun deleteFragment(fragment: Fragment)
    fun backPressCallback(callback: OnBackPressedCallback)


    fun <T: Parcelable> publishResult(result: T)
    fun <T: Parcelable> listenResult(class_: Class<T>, owner: LifecycleOwner, listener: ResultListener<T>)

}