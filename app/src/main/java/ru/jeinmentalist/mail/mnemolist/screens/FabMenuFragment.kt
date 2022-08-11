package ru.jeinmentalist.mail.mnemolist.screens

import android.os.Bundle
import android.view.View
import ru.jeinmentalist.mail.mentalist.databinding.FragmentFabMenuBinding
import ru.jeinmentalist.mail.mnemolist.base.BaseFragment
import ru.jeinmentalist.mail.mnemolist.contract.FirstFragment
import ru.jeinmentalist.mail.mnemolist.contract.Options
import ru.jeinmentalist.mail.mnemolist.utils.ExitWithAnimation
import ru.jeinmentalist.mail.mnemolist.utils.exitCircularReveal
import ru.jeinmentalist.mail.mnemolist.utils.startCircularReveal


class FabMenuFragment : BaseFragment<FragmentFabMenuBinding>(FragmentFabMenuBinding::inflate),
    ExitWithAnimation {

    private var mOptions: Options? = null
    override var posX: Int? = null
    override var posY: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mOptions =
            savedInstanceState?.getParcelable(KEY_OPTIONS)
                ?: arguments?.getParcelable(ARG_OPTIONS)
                        ?: throw IllegalArgumentException("You need to specify options to launch this fragment")
        arguments?.getIntArray(ARG_LIST)?.let {
            posX = it[0]
            posY = it[1]
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.background.background = null
        view.visibility = View.VISIBLE
        view.startCircularReveal(posX!!, posY!!, null)

        binding.createProfile.setOnClickListener {
            (parentFragment as FirstFragment).createProfile()
        }
        binding.createNote.setOnClickListener {
            (parentFragment as FirstFragment).createNote()
        }
    }

    override fun close(callback: ()->Unit) {
        if (checkStatus()){
            view?.exitCircularReveal(posX!!, posY!!) {
                binding.root.removeAllViews()
                view?.visibility = View.GONE
                callback.invoke()
            }
        }
    }

    override fun isToBeExitedWithAnimation(): Boolean = true

    companion object {
        private val ARG_OPTIONS = "ARG_OPTIONS"
        private val ARG_LIST = "ARG_LIST"
        private val KEY_OPTIONS = "KEY_OPTIONS"

        fun newInstance(options: Options, exit: IntArray? = null): FabMenuFragment {
            val args = Bundle()
            args.putParcelable(ARG_OPTIONS, options)
            args.putIntArray(ARG_LIST, exit)
            val fragment = FabMenuFragment()
            fragment.arguments = args
            return fragment
        }
    }
}