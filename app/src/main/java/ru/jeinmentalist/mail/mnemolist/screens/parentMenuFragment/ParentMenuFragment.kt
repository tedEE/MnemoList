package ru.jeinmentalist.mail.mnemolist.screens.parentMenuFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import ru.jeinmentalist.mail.mentalist.R
import ru.jeinmentalist.mail.mentalist.databinding.FragmentParentMenuBinding
import ru.jeinmentalist.mail.mnemolist.base.BaseFragment
import ru.jeinmentalist.mail.mnemolist.contract.BottomNavigator
import ru.jeinmentalist.mail.mnemolist.contract.IOnBackPress
import ru.jeinmentalist.mail.mnemolist.screens.createNote.CreateNoteFragment
import ru.jeinmentalist.mail.mnemolist.screens.createProfile.CreateProfileFragment
import ru.jeinmentalist.mail.mnemolist.screens.profilelist.ProfileListFragment

class ParentMenuFragment :
    BaseFragment<FragmentParentMenuBinding>(FragmentParentMenuBinding::inflate),
    BottomNavigator,
    IOnBackPress {

    lateinit var mMenuItems: Array<CbnMenuItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProfileList()
        createCurvedBottomNavigation()
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
            .addToBackStack(null)
            .replace(R.id.container_menu, fragment)
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
        binding.navView.setMenuItems(mMenuItems, 2)

        binding.navView.setOnMenuItemClickListener { cbnMenuItem, index ->
            when (index) {
                0 -> showCreateProfileFragment()
                1 -> showCreateNoteFragment()
                2 -> showProfileList()
                3 -> {}
                4 -> {}
            }
        }
    }

    override fun onBackPressed(): Boolean {
        binding.navView.performClick()
        return true
    }


}