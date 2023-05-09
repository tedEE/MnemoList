package ru.jeinmentalist.mail.mnemolist.screens.profilelist

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.mentalist.R
import ru.jeinmentalist.mail.mentalist.databinding.FragmentProfileListBinding
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showToast
import ru.jeinmentalist.mail.mnemolist.base.BaseFragment
import ru.jeinmentalist.mail.mnemolist.contract.*
import ru.jeinmentalist.mail.mnemolist.utils.exitReveal


@AndroidEntryPoint
class ProfileListFragment :
    HasCustomTitle,
    IOnBackPress,
    HasFabClickListener,
    BaseFragment<FragmentProfileListBinding>(FragmentProfileListBinding::inflate) {

    private var profileListAdapter = ProfileListAdapter()
    private var mProfileList: List<Profile> = listOf()

    //    var isFabMenuOpen = false
    private val mProfileListViewModel: ProfileListViewModel by viewModels()
//    private var mPositionsFab: IntArray? = null
//    var fabOpen = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        onBackPressed()
//        dischargeFab()
        mProfileListViewModel.profileListLiveData.observe(viewLifecycleOwner, Observer {
            showLog(it.toString())
            mProfileList = it
            profileListAdapter.submitList(it)
        })


//        binding.fab.setOnClickListener {
//            showRevealFabMenu(it)
//        }
    }

    private fun setupRecyclerViewForCard(view: View): ProfileItemAdapter {
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
                    view.findViewById<RecyclerView>(R.id.description_rv)
                val buttonDelete = view.findViewById<MaterialButton>(R.id.button_delete)
                var v = profile_description.visibility
                val icon: AnimatedVectorDrawable

                val constraintSet = ConstraintSet()
                val profileItem = view.findViewById<ConstraintLayout>(R.id.profile_item)
                constraintSet.clone(profileItem)

                if (v == View.GONE) {
                    v = View.VISIBLE
                    buttonDelete.icon =
                        AppCompatResources.getDrawable(requireContext(), R.drawable.short_anim_open)
                    icon = buttonDelete.icon as AnimatedVectorDrawable
                    icon.start()
                    icon.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            buttonDelete.icon = AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.ic_edit_profile
                            )
                        }
                    })

                    constraintSet.connect(
                        R.id.button_delete,
                        ConstraintSet.TOP,
                        R.id.description_rv,
                        ConstraintSet.TOP
                    )
                    constraintSet.connect(
                        R.id.button_delete,
                        ConstraintSet.BOTTOM,
                        R.id.description_rv,
                        ConstraintSet.BOTTOM
                    )
                } else {
                    v = View.GONE
                    buttonDelete.icon = AppCompatResources.getDrawable(
                        requireContext(),
                        R.drawable.short_anim_close
                    )
                    icon = buttonDelete.icon as AnimatedVectorDrawable
                    icon.start()
                    icon.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            buttonDelete.icon = AppCompatResources.getDrawable(
                                requireContext(),
                                R.drawable.ic_delete_profile
                            )
                        }
                    })
                    constraintSet.connect(
                        R.id.button_delete,
                        ConstraintSet.TOP,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.TOP
                    )
                    constraintSet.clear(R.id.button_delete, ConstraintSet.BOTTOM)
                    constraintSet.connect(
                        R.id.button_delete,
                        ConstraintSet.END,
                        ConstraintSet.PARENT_ID,
                        ConstraintSet.END
                    )
                }
                TransitionManager.beginDelayedTransition(this, AutoTransition())
                constraintSet.applyTo(profileItem)
                mProfileListViewModel.getTimestampList(profile.profileId) {
                    val list = it.map {
                        it.translationFromMilliseconds()
                    }
                    setupRecyclerViewForCard(view).setData(list)
                    profile_description.visibility = v
                }
            }
            profileListAdapter.onProfileButtonDeleteClickListener = { profile ->
                mProfileListViewModel.deleteProfile(profile)
            }
        }
    }

    private fun showRevealFabMenu(view: View) {
        view.exitReveal {
//            fabOpen = true
//            binding.childFragmentContainer.visibility = View.VISIBLE
//            binding.childFragmentContainer.layoutParams
//            val positions = view.findLocationOfCenterOnTheScreen()
//            mPositionsFab = positions
//            childFragmentManager.beginTransaction()
//                .add(
//                    R.id.child_fragment_container,
//                    FabMenuFragment.newInstance(Options(mProfileList), positions)
//                )
//                .addToBackStack(null)
//                .commit()
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

//    private fun showFab() {
//        binding.fab.enterReveal()
//    }

    /**
     * реализация методов интерфейса FirstFragment
     */

//    override fun createCarts() {
//        closeMenu {
//        }
//    }

//    override fun createNote() {
//        closeMenu {
//            navigator().showCreateNoteFragment(
//                Options(
////                    volume = mProfileList,
//                    volume = listOf(),
//                    openParams = mPositionsFab ?: intArrayOf()
//                )
//            )
//        }
//    }
//
//    override fun createProfile() {
//        closeMenu {
//            navigator().showCreateProfileFragment(
//                Options(
//                    volume = listOf(),
//                    openParams = mPositionsFab ?: intArrayOf()
//                )
//            )
//        }
//    }

    override fun onBackPressed(): Boolean {
//        return if (fabOpen) {
//            closeMenu()
//            true
//        } else {
//            false
//        }
        return true
    }

    private fun closeMenu(block: () -> Unit = {}) {
//        val fabFragment = childFragmentManager.findFragmentById(R.id.child_fragment_container)
//        (fabFragment as? ExitWithAnimation)?.close {
//            fabOpen = false
//            binding.childFragmentContainer.visibility = View.GONE
//            deleteFabMenuFragment(fabFragment)
//            block.invoke()
//            showFab()
//        }
    }

    override fun onFabClick() {
        showToast(requireContext(), "список профилей")
    }

//    private fun deleteFabMenuFragment(fr: Fragment) {
//        childFragmentManager.beginTransaction().remove(fr).commit()
////        childFragmentManager.executePendingTransactions()
//        childFragmentManager.popBackStack()
//    }
}