package com.hit.software.myapp1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if(intent.action!! == "android.intent.action.BOOT_COMPLETED"){
            val mIntent=Intent(context,MainActivity::class.java)
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(mIntent)
        }
    }
}
