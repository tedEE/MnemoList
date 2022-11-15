package ru.jeinmentalist.mail.mnemolist.screens.checkeNoteAlarm

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import ru.jeinmentalist.mail.mentalist.databinding.FragmentChekcAlarmBinding
import ru.jeinmentalist.mail.mnemolist.base.BaseFragment

@AndroidEntryPoint
class CheckAlarmFragment :
    BaseFragment<FragmentChekcAlarmBinding>(FragmentChekcAlarmBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        
    }

    companion object{
        fun newInstance(): CheckAlarmFragment {
            val args = Bundle()
            val fragment = CheckAlarmFragment()
            fragment.arguments = args
            return fragment
        }
    }
}