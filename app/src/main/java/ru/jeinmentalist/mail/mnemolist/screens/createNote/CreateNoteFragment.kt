package ru.jeinmentalist.mail.mnemolist.screens.createNote

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.mentalist.R
import ru.jeinmentalist.mail.mentalist.databinding.FragmentCreateNoteBinding
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
import ru.jeinmentalist.mail.mnemolist.background.MakeAlarmWorker
import ru.jeinmentalist.mail.mnemolist.base.BaseFragment
import ru.jeinmentalist.mail.mnemolist.contract.*
import ru.jeinmentalist.mail.mnemolist.utils.ExitWithAnimation
import ru.jeinmentalist.mail.mnemolist.utils.exitCircularReveal
import ru.jeinmentalist.mail.mnemolist.utils.startBackgroundColorAnimation
import ru.jeinmentalist.mail.mnemolist.utils.startCircularReveal


@AndroidEntryPoint
class CreateNoteFragment :
    BaseFragment<FragmentCreateNoteBinding>(FragmentCreateNoteBinding::inflate),
    HasCustomTitle,
    HasCustomAction,
    ExitWithAnimation {

    private lateinit var mOptions: Options
    private val mCreateNoteViewModel: CreateNoteViewModel by viewModels()
    private val getContent: ActivityResultLauncher<String> = registerForActivityResult(ActivityResultContracts.GetContent()){ imageUri: Uri? ->
        Picasso.get()
            .load(imageUri)
            .fit()
            .into(binding.imageDescription)
    }

    override var posX: Int? = null
    override var posY: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            mOptions =
                savedInstanceState?.getParcelable(KEY_OPTIONS) ?: arguments?.getParcelable(ARG_OPTIONS)
                        ?: throw IllegalArgumentException("You need to specify options to launch this fragment")
            posX = mOptions.openParams[0]
            posY = mOptions.openParams[1]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var profileList: List<Profile>

        mCreateNoteViewModel.profileListLiveData.observe(viewLifecycleOwner, { listProfile: List<Profile> ->
            profileList = listProfile
            val adapter = SpinnerAdapter(profileList)
            binding.profileSpinner.adapter = adapter
        })

        binding.profileSpinner.onItemSelectedListener = OnItemSelectedListener {
            hideKeyboard(binding.enterDescriptionNote)
            hideKeyboard(binding.enterLocationNote)
        }

        binding.imageButton.setOnClickListener {
//            val intent = Intent().apply {`
//                setType("image/*")
//                setAction(Intent.ACTION_GET_CONTENT)
//            }
            getContent.launch("image/*")
        }

        val animator = binding.rootCreateNote.startBackgroundColorAnimation(
            ContextCompat.getColor(requireContext(), R.color.purple_500),
            ContextCompat.getColor(requireContext(), R.color.ic_launcher_background),
            1000
        )
        view.startCircularReveal(posX!!, posY!!, animator)


        binding.enterLocationNote.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                hideKeyboard(binding.enterLocationNote)
            }
        }

        binding.enterDescriptionNote.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                hideKeyboard(binding.enterDescriptionNote)
            }
        }

//        val profileList = mOptions.volume.map {
//            it as Profile
//        }

    }

    override fun isToBeExitedWithAnimation(): Boolean = true

    override fun close(callback: () -> Unit) {
        view?.startBackgroundColorAnimation(
            ContextCompat.getColor(requireContext(), R.color.ic_launcher_background),
            ContextCompat.getColor(requireContext(), R.color.purple_500),
            1000
        )?.start()
        view?.exitCircularReveal(
            posX!!,
            posY!!,
            null
        ) {
            view?.visibility = View.GONE
            navigator().deleteFragment(this)
        }
    }

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_done,
            textRes = R.string.done,
            onCustomAction = Runnable {
                onConfirmPressed()
            }
        )
    }

    private fun onConfirmPressed() {
        validateLocationInput {
            val location = binding.enterLocationNote.text.toString()
            val description = binding.enterDescriptionNote.text.toString()
            val profile = mOptions.volume[binding.profileSpinner.selectedItemPosition] as Profile
            mCreateNoteViewModel.addNote(
                location,
                description,
                profile.profileId
            )
            /////////////////////////////
            mCreateNoteViewModel.mNoteIdLiveData.observe(viewLifecycleOwner, Observer {
                MakeAlarmWorker.create(
                    requireContext(),
                    intArrayOf(it),
                    MakeAlarmWorker.LAUNCH_CREATION
                )
            })
        }
    }

    override fun getTitleRes(): Int = R.string.create_note

    private fun validateLocationInput(successfulСallback: () -> Unit = {}) {
        if (binding.enterLocationNote.text.toString().isBlank()) {
            binding.enterLocationNote.error =
                resources.getString(R.string.enter_location_helper_text)
            binding.enterLocationNote.requestFocus()
            showKeyboard(binding.enterLocationNote)
        } else {
            successfulСallback()
        }
    }

    companion object {
        private val ARG_OPTIONS = "ARG_OPTIONS"
        private val KEY_OPTIONS = "KEY_OPTIONS"

        fun newInstance(options: Options): CreateNoteFragment {
            val args = Bundle()
            args.putParcelable(ARG_OPTIONS, options)
            val fragment = CreateNoteFragment()
            fragment.arguments = args
            return fragment
        }
    }
}