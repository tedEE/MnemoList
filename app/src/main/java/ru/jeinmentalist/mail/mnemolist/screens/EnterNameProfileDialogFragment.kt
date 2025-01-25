package ru.jeinmentalist.mail.mnemolist.screens

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import ru.jeinmentalist.mail.mentalist.R
import ru.jeinmentalist.mail.mentalist.databinding.InputDialogFragmentBinding
import ru.jeinmentalist.mail.mnemolist.contract.Options
import ru.jeinmentalist.mail.mnemolist.contract.navigator

class EnterNameProfileDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = InputDialogFragmentBinding.inflate(layoutInflater)
//        val listener = DialogInterface.OnClickListener { _, which ->
//            parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_RESPONSE to which))
//        }

        val dialog = AlertDialog.Builder(requireContext())
            .setCancelable(false)
            .setTitle("setTitle")
            .setView(binding.root)
            .setPositiveButton("yes", null)
//            .setNegativeButton("no", listener)
//            .setNeutralButton("ignore", listener)
            .create()

        dialog.setOnShowListener{
            binding.enterNameProfile.requestFocus()
            showKeyboard(binding.enterNameProfile)
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener{
                val enteredText = binding.enterNameProfile.text.toString()
                if (enteredText.isBlank()) {
                    binding.enterNameProfile.error = getString(R.string.enter_name_profile_helper_text)
                    return@setOnClickListener
                }
//                navigator().publishResult<Options>(Options(enteredText))
                navigator().showCreateProfileFragment()
//                parentFragmentManager.setFragmentResult(requestKey, bundleOf(KEY_VOLUME_RESPONSE to volume))
                dismiss()
            }
        }

        dialog.setOnDismissListener { hideKeyboard(binding.enterNameProfile) }
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d(TAG, "Dialog dismissed")
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        Log.d(TAG, "Dialog onCancel")
    }

    private fun showKeyboard(view: View) {
        view.post{
            getInputMethodManager(view).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun hideKeyboard(view: View) {
        getInputMethodManager(view).hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getInputMethodManager(view: View): InputMethodManager {
        val context = view.context
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    companion object {
        val TAG = EnterNameProfileDialogFragment::class.java.simpleName
        val REQUEST_KEY = "$TAG:defaultRequestKey"
        val KEY_RESPONSE = "RESPONSE"
    }
}