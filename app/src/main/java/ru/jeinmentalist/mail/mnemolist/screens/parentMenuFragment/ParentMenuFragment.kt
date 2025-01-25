package ru.jeinmentalist.mail.mnemolist.screens.parentMenuFragment

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import ru.jeinmentalist.mail.mentalist.R
import ru.jeinmentalist.mail.mentalist.databinding.FragmentParentMenuBinding
import ru.jeinmentalist.mail.mnemolist.MainActivity
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showToast
import ru.jeinmentalist.mail.mnemolist.base.BaseFragment
import ru.jeinmentalist.mail.mnemolist.contract.*
import ru.jeinmentalist.mail.mnemolist.screens.createNote.CreateNoteFragment
import ru.jeinmentalist.mail.mnemolist.screens.createProfile.CreateProfileFragment
import ru.jeinmentalist.mail.mnemolist.screens.profilelist.ProfileListFragment
import ru.jeinmentalist.mail.mnemolist.utils.setFabTouchListener

class ParentMenuFragment :
    BaseFragment<FragmentParentMenuBinding>(FragmentParentMenuBinding::inflate),
    BottomNavigator,
    IOnBackPress {

    private lateinit var mMenuItems: Array<CbnMenuItem>
    private var currentScrins: Int = 2

    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {

        override fun onFragmentViewCreated(
            fm: FragmentManager,
            f: Fragment,
            v: View,
            savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            updateUI(f)
        }
    }

    private val currentFragment: Fragment
        get() = parentFragmentManager.findFragmentById(binding.containerMenu.id)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, false)
        showProfileList()
        createCurvedBottomNavigation()

    }

    private fun updateUI(fragment: Fragment) {

        val actionCreator = (activity as CustomActionCreator)
        val titleCreator = (activity as CustomTitleCreator)

        if (fragment is HasCustomTitle) {
            titleCreator.updateTitle(fragment.getTitleRes())
        } else {
            titleCreator.updateTitle(R.string.app_name)
        }

        if (fragment is HasCustomAction) {
            actionCreator.createCustomToolbarAction(fragment.getCustomAction())
        } else {
            actionCreator.clearToolbarAction()
        }
    }


    override fun showCreateProfileFragment() {
        launchFragment(CreateProfileFragment.newInstance())
    }

    override fun showCreateNoteFragment() {
        launchFragment(CreateNoteFragment.newInstance())
    }

    override fun showProfileList() {
        launchFragment(ProfileListFragment())
    }

    private fun launchFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .setCustomAnimations(  // нужнор создать анимации переходов
                R.anim.slide_in_navigation,
                R.anim.fade_out_navigation,
                R.anim.fade_in_navigation,
                R.anim.slide_out_navigation
            )
            // при вызове replace предыдущий фрагмент заменяеться
            // при вызове add новый фрагмент накладываеться поверх предыдущего
            .add(R.id.container_menu, fragment)
            .commit()
    }

    private fun createCurvedBottomNavigation() {
        mMenuItems = arrayOf(
            CbnMenuItem(
                R.drawable.ic_avd_category, // the icon
                R.drawable.avd_category, // the AVD that will be shown in FAB
            ),
            CbnMenuItem(
                R.drawable.ic_avd_note,
                R.drawable.avd_note,
            ),
            CbnMenuItem(
                R.drawable.ic_avd_add,
                R.drawable.avd_add,
            ),
            CbnMenuItem(
                R.drawable.ic_settings,
                R.drawable.avd_settings,
            ),
            CbnMenuItem(
                R.drawable.ic_edit_profile,
                R.drawable.profile_anim_close,
            )
        )

        binding.navView.setMenuItems(mMenuItems, currentScrins)

        binding.navView.setFabTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val fabClickListener = (currentFragment as HasFabClickListener)
                    fabClickListener.onFabClick()
                    true
                }
                MotionEvent.ACTION_UP -> {
                    true
                }
                else -> false
            }
        }

        binding.navView.setOnMenuItemClickListener { cbnMenuItem, index ->
            when (index) {
                0 -> {
                    currentScrins = 0
                    showCreateProfileFragment()
                }
                1 -> {
                    currentScrins = 1
                    showCreateNoteFragment()
                }
                2 -> {
                    currentScrins = 2
                    showProfileList()
                }
                3 -> currentScrins = 3
                4 -> currentScrins = 4
            }
        }
    }

    override fun onBackPressed(): Boolean {
        if (currentScrins != 2){
            binding.navView.onMenuItemClick(2)
            return true
        }
        return false
    }


}