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

class DeleteNoteDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val listener = DialogInterface.OnClickListener { _, which ->
            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESPONSE to which))
        }
        return AlertDialog.Builder(requireContext())
            .setCancelable(true)
//            .setTitle(getString(R.string.delete_note_title_dialog_fragment))
            .setMessage(getString(R.string.delete_note_message_dialog_fragment))
            .setNegativeButton(getString(R.string.no), listener)
            .setPositiveButton(getString(R.string.yeas), listener)
            .create()
    }

    companion object {
        val TAG = DeleteNoteDialogFragment::class.java.simpleName
        val REQUEST_KEY = "$TAG:defaultRequestKey"
        val KEY_RESPONSE = "RESPONSE"

        fun show(manager: FragmentManager) {
            val dialogFragment = DeleteNoteDialogFragment()
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