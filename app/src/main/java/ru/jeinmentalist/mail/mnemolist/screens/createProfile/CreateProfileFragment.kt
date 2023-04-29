package ru.jeinmentalist.mail.mnemolist.screens.createProfile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.jeinmentalist.mail.domain.timestamp.Timestamp
import ru.jeinmentalist.mail.mentalist.R
import ru.jeinmentalist.mail.mentalist.databinding.FragmentCreateProfileBinding
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showToast
import ru.jeinmentalist.mail.mnemolist.base.BaseFragment
import ru.jeinmentalist.mail.mnemolist.contract.*
import ru.jeinmentalist.mail.mnemolist.utils.*
import java.util.*


@AndroidEntryPoint
class CreateProfileFragment : BaseFragment<FragmentCreateProfileBinding>(
    FragmentCreateProfileBinding::inflate
), HasCustomAction{

    private val createProfileViewModel: CreateProfileViewModel by viewModels()
    private var timestampExecutionTimeText: Long? = null
    private var timestampExecutionTimeNumber: Long? = null
    private val timestampList = mutableListOf<Timestamp>()
    private var idProfile: String = ""

    private var mOptions: Options? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animator = binding.scrollView.startBackgroundColorAnimation(
            ContextCompat.getColor(requireContext(), R.color.purple_500),
            ContextCompat.getColor(requireContext(), R.color.ic_launcher_background),
            1000
        )
//        createProfileViewModel = ViewModelProvider(this)[CreateProfileViewModel::class.java]
        idProfile = createIdDb()
//        navigator().listenResult(Options::class.java, viewLifecycleOwner){
//            showToast(requireContext(), it.toString())
//        }
        initEditTextTimeInterval()
        createSpinnerText()
        binding.enterNameProfile.requestFocus()

        val rv = binding.timestamps
        val adapter = TimeStampAdapter()
        rv.adapter = adapter

//        navigator().listenResult(Options::class.java, viewLifecycleOwner){
//            showToast(requireContext(), it.volume)
//        }

        binding.enterNameProfile.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                hideKeyboard(binding.enterNameProfile)
            }
        }

        binding.enterNameProfile.setOnClickListener {
            binding.enterNameProfileContainer.error = null
        }

        binding.addTimestampButton.setOnClickListener {
            if (timestampExecutionTimeNumber != null && timestampExecutionTimeText != null) {
                val t = Timestamp(
                    idProfile,
                    0,
                    (timestampExecutionTimeNumber!! * timestampExecutionTimeText!!)
                )
                timestampList.add(t)
                adapter.addItem(t)
                Log.d("lsdfkjsdfkljs", timestampList.toString())
                timestampExecutionTimeText = null
                timestampExecutionTimeNumber = null
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // сюда возможно прийдеться вынести логику спинера
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
        if (binding.enterNameProfile.text.toString().isBlank()) {
            validNameProfile {
                hideKeyboard(binding.enterNameProfile)
            }
        } else {
            val nameProfile: String = binding.enterNameProfile.text.toString()
            createProfileViewModel.createProfile(nameProfile, idProfile, timestampList)
        }
    }

    private fun validNameProfile(successfulСallback: () -> Unit = {}) {
        val nameProfile = binding.enterNameProfile.text.toString()
        if (nameProfile.isBlank()) {
            binding.enterNameProfileContainer.error =
                resources.getString(R.string.enter_name_profile_helper_text)
            binding.enterNameProfile.requestFocus()
            showKeyboard(binding.enterNameProfile)
        } else {
            binding.enterNameProfileContainer.error = null
            successfulСallback()
        }
    }

//    private fun setupRecyclerView() {
//        val rv = binding.timestamps
//        val adapter = TimeStampAdapter()
//        rv.adapter = adapter
//    }

    /////////////////////////// методы работы со спинерами
    private fun initEditTextTimeInterval() {
        binding.autoCompleteNumber.setText("--")
        binding.autoCompleteText.setText("--")
        // задать фокус на ввод текста
        binding.autoCompleteText.requestFocus()
    }

    private fun createSpinnerNumber(position: Int) {
        val timeInterval =
            selectElementFromTimeInterval(createProfileViewModel.mapKeyToInt[position])
        binding.autoCompleteNumber.setText("0")
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.drop_down_item, timeInterval.second.toList())
        binding.autoCompleteNumber.setAdapter(arrayAdapter)
        binding.autoCompleteNumber.onItemClickListener =
            AdapterView.OnItemClickListener { parent,
                                              view,
                                              position,
                                              id ->
                timestampExecutionTimeNumber = timeInterval.second.toList()[position].toLong()
                showToast(requireContext(), timeInterval.second.toList()[position].toString())
            }
    }

    private fun createSpinnerText() {
        val arrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.drop_down_item,
            createProfileViewModel.mapKeyToString
        )
        binding.autoCompleteText.setAdapter(arrayAdapter)
        binding.autoCompleteText.onItemClickListener =
            AdapterView.OnItemClickListener { parent,
                                              view,
                                              position,
                                              id ->
                createSpinnerNumber(position)
                val pair = selectElementFromTimeInterval(
                    createProfileViewModel.mapKeyToInt[position]
                )
                timestampExecutionTimeText = pair.first

                Toast.makeText(requireContext(), pair.first.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun createIdDb(): String = UUID.randomUUID().toString()


    companion object {

        fun newInstance(): CreateProfileFragment {
            val fragment = CreateProfileFragment()
            return fragment
        }
    }
}