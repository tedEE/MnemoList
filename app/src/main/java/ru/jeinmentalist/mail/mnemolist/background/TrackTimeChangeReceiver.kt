package ru.jeinmentalist.mail.mnemolist.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.jeinmentalist.mail.mnemolist.UI.utilits.createRebootNotification

class TrackTimeChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        createRebootNotification(context, "Время изменено")
//        val action = intent.action
//        if (Intent.ACTION_TIME_CHANGED == action){
//            sendLastNotification(context)
//        }
    }
}