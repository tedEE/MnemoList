package ru.jeinmentalist.mail.mnemolist.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import ru.jeinmentalist.mail.mnemolist.UI.utilits.sendNotification
import ru.jeinmentalist.mail.mnemolist.UI.utilits.showToast

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        sendNotification("BootReceiver", "ребут устройства", 1, context!!, "")
        BootWorker.create(context)
//        val workManager = WorkManager.getInstance(context!!)
//        workManager.enqueueUniqueWork(
//            BootWorker.WORK_NAME,
//            ExistingWorkPolicy.APPEND_OR_REPLACE,
//            BootWorker.makeRequest())
    }
}