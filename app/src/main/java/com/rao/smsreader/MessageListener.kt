package com.rao.smsreader

import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Telephony
import android.support.v4.app.NotificationCompat
import android.telephony.SmsMessage
import android.util.Log
import android.R.attr.data
import android.support.v4.app.NotificationCompat.getExtras
import android.text.TextUtils
import android.R.id.message
import android.R
import android.app.*
import android.util.DebugUtils
import android.widget.Toast
import android.app.PendingIntent
import android.R.id.message

class MessageListener : BroadcastReceiver() {
    private var msgBody: String? = null
    lateinit var msg_from: String
    private var msg_name:String?=null
    private val preferences: SharedPreferences? = null

    companion object {
        val ACTION1:String="Action"
    }
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle = intent.extras
            var msgs: Array<SmsMessage?>? = null

            if (bundle != null) {
                try {
                    val pdus: Array<Any> = bundle.get("pdus") as Array<Any>
                    msgs = arrayOfNulls<SmsMessage>(pdus.size)
                    for (i in msgs.indices) {
                        msgs[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                        msg_from = msgs[i]!!.originatingAddress
                        msgBody = msgs[i]!!.messageBody
                        msg_name = getContactName(msg_from, context)

                    }
                } catch (e: Exception) {

                }

            }

        }

         if(msg_name==""||TextUtils.isEmpty(msg_name))
        {
            msg_name=msg_from
        }

        val ansintent = Intent(context, MainActivity::class.java)
        // Send data to NotificationView Class
        ansintent.putExtra("title", "e")
        ansintent.putExtra("text", message)
        val pIntent = PendingIntent.getActivity(context, 0, ansintent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(context, "M_CH_ID")
                .setSmallIcon(R.drawable.ic_dialog_alert)
                .setContentTitle(msg_name)
                .setContentText(msgBody)

        Log.d("broadcast", msgBody!!.toString())
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(0, builder.build())

    }
    fun getContactName(phoneNumber: String, context: Context): String {
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))

        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)

        var contactName = ""
        val cursor = context.getContentResolver().query(uri, projection, null, null, null)

        if (cursor != null) {
            if (cursor!!.moveToFirst()) {
                contactName = cursor!!.getString(0)
            }
            cursor!!.close()
        }

        return contactName
    }
    class NotificationServices : IntentService(NotificationServices::class.java.name) {

        override fun onHandleIntent(intent: Intent?) {
            val action:String
            action=intent!!.action
            if(action.equals(ACTION1))
            {
                Toast.makeText(this,"Hello ",Toast.LENGTH_SHORT).show()
            }
        }
    }

}

