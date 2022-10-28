package ru.jeinmentalist.mail.mnemolist.screens.ListAllNote

import android.os.Bundle
import android.view.View
import ru.jeinmentalist.mail.mentalist.databinding.FragmentListAllNoteBinding
import ru.jeinmentalist.mail.mnemolist.base.BaseFragment

class ListAllNoteFragment :
    BaseFragment<FragmentListAllNoteBinding>(FragmentListAllNoteBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
    
    companion object{
        fun newInstance(): ListAllNoteFragment {
            val args = Bundle()
            val fragment = ListAllNoteFragment()
            fragment.arguments = args
            return fragment
        }
    }
}