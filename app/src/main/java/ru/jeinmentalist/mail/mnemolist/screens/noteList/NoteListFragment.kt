package ru.jeinmentalist.mail.mnemolist.screens.noteList

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import ru.jeinmentalist.mail.domain.note.Note
import ru.jeinmentalist.mail.domain.profile.Profile
import ru.jeinmentalist.mail.mentalist.databinding.FragmentNoteListBinding
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showToast
import ru.jeinmentalist.mail.mnemolist.contract.Options
import ru.jeinmentalist.mail.mnemolist.base.BaseFragment

@AndroidEntryPoint
class NoteListFragment : BaseFragment<FragmentNoteListBinding>(FragmentNoteListBinding::inflate) {

    private var mOptions: Options? = null
    private val mNoteListViewModel: NoteListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mOptions =
            savedInstanceState?.getParcelable(KEY_OPTIONS)
                ?: arguments?.getParcelable(ARG_OPTIONS)
                        ?: throw IllegalArgumentException("You need to specify options to launch this fragment")

        val list = mOptions?.let {
            it.volume.map { it as Profile }
        }
        val profile = list?.get(0)
        mNoteListViewModel.getListNote(profile!!.profileId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mNoteListViewModel.notListLiveData.observe(viewLifecycleOwner, Observer {
            initialRecyclerView(it)
        })
//        setupDeleteDialogFragmentListener()
    }

    private fun initialRecyclerView(list: List<Note>?) {
        val rv = binding.rvNote
        val linerLayoutManager = LinearLayoutManager(requireContext())
        // стандартный разделитеть
        val dividerItemDecoration = DividerItemDecoration(requireContext(), linerLayoutManager.orientation)
        with(rv) {
            val notesAdapter = NoteListAdapter()
            layoutManager = linerLayoutManager
            adapter = notesAdapter
            addItemDecoration(dividerItemDecoration)
            itemAnimator = DefaultItemAnimator()
            notesAdapter.submitList(list)
            notesAdapter.deleteButtonClickListener = {
                showDeleteNoteDialogFragment()
                // надо вынести вызов метода в onCreate иначе данные note теряеться при переворачивании экрана
                setupDeleteDialogFragmentListener(it)
            }
            notesAdapter.onItemNoteListClickListener = {
                showDescriptionDialogFragment(it)
            }
        }
    }

    private fun showDescriptionDialogFragment(note: Note){
        ShowDescriptionNoteDialogFragment.show(parentFragmentManager, note.description)
    }

    private fun showDeleteNoteDialogFragment(){
        DeleteNoteDialogFragment.show(parentFragmentManager)
    }

    private fun setupDeleteDialogFragmentListener(note: Note){
        DeleteNoteDialogFragment.setupListener(parentFragmentManager, viewLifecycleOwner){
            when (it) {
                DialogInterface.BUTTON_POSITIVE -> mNoteListViewModel.deleteNote(note)
                DialogInterface.BUTTON_NEGATIVE -> showToast(requireContext(), "Удаление отменено")
            }
        }
    }

    companion object {
        private val ARG_OPTIONS = "ARG_OPTIONS"
        private val KEY_OPTIONS = "KEY_OPTIONS"

        fun newInstance(options: Options): NoteListFragment {
            val args = Bundle()
            args.putParcelable(ARG_OPTIONS, options)
            val fragment = NoteListFragment()
            fragment.arguments = args
            return fragment
        }
    }
}