package ru.jeinmentalist.mail.mnemolist.screens.noteList

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import ru.jeinmentalist.mail.mentalist.R

class ShowDescriptionNoteDialogFragment : DialogFragment(){

    private val volume: String
        get() = requireArguments().getString(ARG_VOLUME) ?: ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val listener = DialogInterface.OnClickListener { _, _ ->

        }
        return AlertDialog.Builder(requireContext())
            .setCancelable(true)
            .setTitle(getString(R.string.description_note_dialog_title))

            .setMessage(volume)
            .setPositiveButton(getString(R.string.ok), listener)
            .create()
    }

    companion object {
        val TAG = ShowDescriptionNoteDialogFragment::class.java.simpleName
        val REQUEST_KEY = "$TAG:defaultRequestKey"
        val KEY_RESPONSE = "RESPONSE"
        private val ARG_VOLUME = "ARG_VOLUME"

        fun show(manager: FragmentManager, volume: String) {
            val dialogFragment = ShowDescriptionNoteDialogFragment()
            dialogFragment.arguments = bundleOf(ARG_VOLUME to volume)
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: (Int) -> Unit
        ) {
            manager.setFragmentResultListener(
                REQUEST_KEY,
                lifecycleOwner,
                FragmentResultListener { _, result ->
                    listener.invoke(result.getInt(KEY_RESPONSE))
                })
        }
    }
}