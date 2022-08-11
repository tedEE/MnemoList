package ru.jeinmentalist.mail.mnemolist.screens.profilelist

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.mentalist.R
import ru.jeinmentalist.mail.mentalist.databinding.FragmentProfileListBinding
import ru.jeinmentalist.mail.mnemolist.base.BaseFragment
import ru.jeinmentalist.mail.mnemolist.contract.*
import ru.jeinmentalist.mail.mnemolist.screens.FabMenuFragment
import ru.jeinmentalist.mail.mnemolist.utils.ExitWithAnimation
import ru.jeinmentalist.mail.mnemolist.utils.enterReveal
import ru.jeinmentalist.mail.mnemolist.utils.exitReveal
import ru.jeinmentalist.mail.mnemolist.utils.findLocationOfCenterOnTheScreen


@AndroidEntryPoint
class ProfileListFragment :
    HasCustomTitle,
    FirstFragment,
    IOnBackPress,
    BaseFragment<FragmentProfileListBinding>(FragmentProfileListBinding::inflate) {

    private var profileListAdapter = ProfileListAdapter()
    private var mProfileList: List<Profile> = listOf()
    var isFabMenuOpen = false
    private val mProfileListViewModel: ProfileListViewModel by viewModels()
    private var mPositionsFab: IntArray? = null
    var fabOpen = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        onBackPressed()
//        dischargeFab()
        hideFabItem()
        mProfileListViewModel.profileListLiveData.observe(viewLifecycleOwner, Observer {
            mProfileList = it
            profileListAdapter.submitList(it)
        })

        binding.fabMenuItem1.setOnClickListener {
            isFabMenuOpen = false
//            closeFabMenu(300)
        }
        binding.fabMenuItem2.setOnClickListener {
            isFabMenuOpen = false
//            navigator().showCreateProfileFragment()
        }
        binding.fabMenuItem3.setOnClickListener {
            isFabMenuOpen = false
            navigator().showCreateNoteFragment(Options(mProfileList))

        }

        binding.fab.setOnClickListener {
            showRevealFabMenu(it)
//            if (!isFabMenuOpen) {
//                showFabMenu(300)
//            } else {
//                closeFabMenu(300)
//            }
        }
    }

    private fun setupRecyclerViewForCard(view: View,): ProfileItemAdapter {
        val ad = ProfileItemAdapter()
        val rv = view.findViewById<RecyclerView>(R.id.description_rv)
        rv.adapter = ad
        rv.layoutManager = LinearLayoutManager(requireContext())
        return ad
    }

    private fun setupRecyclerView() {
        val rv = binding.profileList
        with(rv) {
            adapter = profileListAdapter
            recycledViewPool.setMaxRecycledViews(
                ProfileListAdapter.TypeProfile.LIST.idType,
                ProfileListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ProfileListAdapter.TypeProfile.CART.idType,
                ProfileListAdapter.MAX_POOL_SIZE
            )

            profileListAdapter.onProfileItemClickListener = {
                showNoteListFragment(it)
            }

            profileListAdapter.onProfileItemLongClickListener = { profile, view ->
                val profile_description =
                    view.findViewById<ConstraintLayout>(R.id.profile_description)
                var v = profile_description.visibility
                if (v == View.GONE) {
                    v = View.VISIBLE
                } else {
                    v = View.GONE
                }
                TransitionManager.beginDelayedTransition(this, AutoTransition())
                mProfileListViewModel.getTimestampList(profile.profileId) {
                    val list = it.map {
                        it.translationFromMilliseconds()
                    }
                    setupRecyclerViewForCard(view).setData(list)
//                    listTimestamp.text = list.joinToString(separator = "") {
//                        "$it \n"
//                    }
                    profile_description.visibility = v
//                    listTimestamp.layoutParams.height = 500
                }
            }
            profileListAdapter.onProfileButtonDeleteClickListener = { profile ->
                mProfileListViewModel.deleteProfile(profile)
            }
        }
    }

    private fun showRevealFabMenu(view: View) {
        view.exitReveal {
            fabOpen = true
            binding.childFragmentContainer.visibility = View.VISIBLE
            val positions = view.findLocationOfCenterOnTheScreen()
            mPositionsFab = positions
            childFragmentManager.beginTransaction()
                .add(
                    R.id.child_fragment_container,
                    FabMenuFragment.newInstance(Options(mProfileList), positions)
                )
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showNoteListFragment(profile: Profile) {
        navigator().showListNoteFragment(Options(listOf(profile)))
    }

    override fun getTitleRes(): Int {
        return R.string.list_profiles
    }

    override fun onDestroy() {
        super.onDestroy()
        profileListAdapter.onProfileItemClickListener = null
        profileListAdapter.onProfileItemLongClickListener = null
        profileListAdapter.onProfileButtonDeleteClickListener = null
    }

    private fun showFab() {
        binding.fab.enterReveal()
    }

    /**
     * реализация методов интерфейса FirstFragment
     */

    override fun createNote() {
        closeMenu {
            navigator().showCreateNoteFragment(
                Options(
                    volume = mProfileList,
                    openParams = mPositionsFab ?: intArrayOf()
                ))
        }
    }

    override fun createProfile() {
        closeMenu {
            navigator().showCreateProfileFragment(
                Options(
                    volume = listOf(),
                    openParams = mPositionsFab ?: intArrayOf()
                )
            )
        }
    }

    override fun onBackPressed():Boolean {
        return if (fabOpen){
            closeMenu()
            true
        }else{
            false
        }
    }

    private fun closeMenu(block: () -> Unit = {}) {
        val fabFragment = childFragmentManager.findFragmentById(R.id.child_fragment_container)
        (fabFragment as? ExitWithAnimation)?.close{
            fabOpen = false
            binding.childFragmentContainer.visibility = View.GONE
            deleteFabMenuFragment(fabFragment)
            block.invoke()
            showFab()
        }
    }

    private fun deleteFabMenuFragment(fr: Fragment){
        childFragmentManager.beginTransaction().remove(fr).commit()
//        childFragmentManager.executePendingTransactions()
        childFragmentManager.popBackStack()
    }
}