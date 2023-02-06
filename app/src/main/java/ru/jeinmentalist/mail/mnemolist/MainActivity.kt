package ru.jeinmentalist.mail.mnemolist

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.AndroidEntryPoint
import ru.jeinmentalist.mail.mentalist.R
import ru.jeinmentalist.mail.mentalist.databinding.ActivityMainBinding
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
import ru.jeinmentalist.mail.mnemolist.contract.*
import ru.jeinmentalist.mail.mnemolist.screens.createNote.CreateNoteFragment
import ru.jeinmentalist.mail.mnemolist.screens.createProfile.CreateProfileFragment
import ru.jeinmentalist.mail.mnemolist.screens.noteList.NoteListFragment
import ru.jeinmentalist.mail.mnemolist.contract.Options
import ru.jeinmentalist.mail.mnemolist.screens.ListAllNote.ListAllNoteFragment
import ru.jeinmentalist.mail.mnemolist.screens.profilelist.ProfileListFragment
import ru.jeinmentalist.mail.mnemolist.utils.ExitWithAnimation

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), Navigator {
    lateinit var binding: ActivityMainBinding

    val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
    val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
            super.onFragmentViewDestroyed(fm, f)
            updateUI()
        }
        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
//        deleteDataBase()
        setSupportActionBar(binding.toolbar)
        // регистрация первого фрагмент при первом запуске, в дальнейшем фрагмент нужно будет поменять
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment, ProfileListFragment())
                .commit()
        }
        // регистрация слушателя смены фрагментов
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
    }

    override fun onBackPressed() {
        when (currentFragment) {
            is IOnBackPress -> {
                if (!(currentFragment as IOnBackPress).onBackPressed()){
                    super.onBackPressed()
                }
            }
            is ExitWithAnimation -> {
                (currentFragment as ExitWithAnimation).close()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // вроде как необходимо для работы кнопки назад в тулбаре, надо будет разобраться
        onBackPressed()
        return true
    }

    override fun onResume() {
        super.onResume()
        App.appResumed()
    }

    override fun onPause() {
        super.onPause()
        App.appPaused()
    }

    override fun onDestroy() {
        super.onDestroy()
        // удаление слушателя фрагментов
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    /**
     *                                      дополнительные методы
     */

    private fun updateUI() {
        val fragment = currentFragment

        if (fragment is HasCustomTitle) {
            binding.toolbar.title = getString(fragment.getTitleRes())
        } else {
            binding.toolbar.title = getString(R.string.app_name)
        }

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setDisplayShowHomeEnabled(false)
        }

        if (fragment is HasCustomAction) {
            createCustomToolbarAction(fragment.getCustomAction())
        } else {
            binding.toolbar.menu.clear()
        }
    }

    private fun createCustomToolbarAction(action: CustomAction) {
        binding.toolbar.menu.clear() // clearing old action if it exists before assigning a new one

        val iconDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(this, action.iconRes)!!)
        iconDrawable.setTint(Color.WHITE)

        val menuItem = binding.toolbar.menu.add(action.textRes)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = iconDrawable
        menuItem.setOnMenuItemClickListener {
            action.onCustomAction.run()
            return@setOnMenuItemClickListener true
        }
    }
    // добавить private
    fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(  // нужнор создать анимации переходов
                R.anim.slide_in_navigation,
                R.anim.fade_out_navigation,
                R.anim.fade_in_navigation,
                R.anim.slide_out_navigation
            )
            .addToBackStack(null)
            // при вызове replace предыдущий фрагмент заменяеться
            // при вызове add новый фрагмент накладываеться поверх предыдущего
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    private fun addFragment(fragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .add(R.id.nav_host_fragment, fragment)
            .commit()
    }

    fun deleteDataBase(nameDB: String = "mnemo_list_database"){
        applicationContext.deleteDatabase(nameDB)
    }

    /**
     *                                    реализация методов Navigator
     */

    override fun showListNote() {
        addFragment(ListAllNoteFragment.newInstance())
    }

    override fun goToFirstFragment() {
        // метод удаляет все фрагмены в стеке кроме самого первого
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun deleteFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().remove(fragment).commit()
        goToFirstFragment()
    }

    override fun showCreateProfileFragment(options: Options) {
        addFragment(CreateProfileFragment.newInstance(options))
    }

    override fun showCreateNoteFragment(options: Options) {
        // создание заметки и редоктирование можно реализовать в одном фрагменте
        // через разные статические методы newInstance
        addFragment(CreateNoteFragment.newInstance(options))
    }

    override fun showListNoteFragment(options: Options) {
        launchFragment(NoteListFragment.newInstance(options))
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun <T : Parcelable> publishResult(result: T) {
        supportFragmentManager.setFragmentResult(
            result.javaClass.name,
            bundleOf(KEY_RESULT to result)
        )
    }

    override fun <T : Parcelable> listenResult(
        class_: Class<T>,
        owner: LifecycleOwner,
        listener: ResultListener<T>
    ) {
        supportFragmentManager.setFragmentResultListener(
            class_.name,
            owner,
            FragmentResultListener { key, bundle ->
                listener.invoke(bundle.getParcelable(KEY_RESULT)!!)
            })
    }

    override fun backPressCallback(callback: OnBackPressedCallback) {
        onBackPressedDispatcher.addCallback(callback)
    }

    companion object {
        private val KEY_RESULT = "RESULT"
    }
}