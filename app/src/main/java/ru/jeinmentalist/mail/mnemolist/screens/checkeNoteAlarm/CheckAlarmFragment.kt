package ru.jeinmentalist.mail.mnemolist.screens.checkeNoteAlarm

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.jeinmentalist.mail.data.db.MnemoListDatabase
import ru.jeinmentalist.mail.data.db.dbForMobile
import ru.jeinmentalist.mail.mentalist.databinding.FragmentChekcAlarmBinding
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showLog
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showToast
import ru.jeinmentalist.mail.mnemolist.base.BaseFragment

@AndroidEntryPoint
class CheckAlarmFragment :
    BaseFragment<FragmentChekcAlarmBinding>(FragmentChekcAlarmBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // удалить
        lifecycleScope.launch(Dispatchers.IO){
            val database = Room.databaseBuilder(
                requireContext(),
                MnemoListDatabase::class.java,
                dbForMobile
            )
//            .addCallback(MnemoListDatabase.DB_CALLBACK)
                .build()
            val dao = database.noteDao()
            val map = dao.getNoteAndTimestampList(12)
            withContext(Dispatchers.Main){
                showLog("${map}")
            }

        }
        ////////////////////////////////////////////////////
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