package com.rao.smsreader

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.Period
import java.util.*

import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    lateinit var lv1: RecyclerView
    var smsZero = ArrayList<SMSData>()
    var smsOne = ArrayList<SMSData>()
    var smsTwo = ArrayList<SMSData>()
    var smsThree = ArrayList<SMSData>()
    var smsSix = ArrayList<SMSData>()
    var smsTwelve = ArrayList<SMSData>()
    var smsDay = ArrayList<SMSData>()
    var smsFinal = ArrayList<SMSData>()
//    lateinit varr broadcastReceiver:BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lv1 = findViewById<View>(R.id.recyViewSMS) as RecyclerView
        lv1.layoutManager= LinearLayoutManager(this@MainActivity) as RecyclerView.LayoutManager?
        if(!checkPermission())
        {
            requestPermission(PERMISSION_REQUEST_CODE)
        }
}
    fun readAllMessage():ArrayList<SMSData>{
        val uriSMSURI = Uri.parse("content://sms/inbox")
        val cur = contentResolver.query(uriSMSURI, null, null, null, null)!!

        while (cur.moveToNext()) {
            //String address = cur.getString(cur.getColumnIndex("address"));
            val body = cur.getString(cur.getColumnIndexOrThrow("body"))
            val address = cur.getString(cur.getColumnIndexOrThrow("address"))
            val millis = cur.getString(cur.getColumnIndexOrThrow("date"))
            var name:String=getContactName(address,this)

            val timestamp = java.lang.Long.parseLong(millis)
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timestamp
            val finaldate = calendar.time
             Log.d("date",finaldate.toString())


            val rightNow = Calendar.getInstance()
            val rightnowdate:Date=rightNow.time
            Log.d("date",rightnowdate.toString())


            val hours:Long=Diff(rightnowdate,finaldate)
            Log.d("diff",hours.toString())
//
            if(abs(hours)<1)
            {
                if(name==""||TextUtils.isEmpty(name))
                {
                    name=address
                }
                smsZero.add(SMSData(body, name, hours))
            }
            if(abs(hours)<2&& abs(hours)>=1)
            {
                if(name==""||TextUtils.isEmpty(name))
                {
                    name=address
                }
                smsOne.add(SMSData(body, name, hours))
            }
            if(abs(hours)<3&& abs(hours)>=2)
            {
                if(name==""||TextUtils.isEmpty(name))
                {
                    name=address
                }
                smsTwo.add(SMSData(body, name, hours))
            }
            if(abs(hours)<6&& abs(hours)>=3)
            {
                if(name==""||TextUtils.isEmpty(name))
                {
                    name=address
                }
                smsThree.add(SMSData(body, name, hours))
            }
            if(abs(hours)<12&& abs(hours)>=6)
            {
                if(name==""||TextUtils.isEmpty(name))
                {
                    name=address
                }
                smsSix.add(SMSData(body, name, hours))
            }
            if(abs(hours)<24&& abs(hours)>=12)
            {
                if(name==""||TextUtils.isEmpty(name))
                {
                    name=address
                }
                smsTwelve.add(SMSData(body, name, hours))
            }
            if(abs(hours)>=24&& abs(hours)<36)
            {
                if(name==""||TextUtils.isEmpty(name))
                {
                    name=address
                }
                smsDay.add(SMSData(body, name, hours))
            }
        }
        smsFinal.add(SMSData("","0 hours ago",0))
        smsFinal.addAll(smsZero)
        smsFinal.add(SMSData("","1 hours ago",1))
        smsFinal.addAll(smsOne)
        smsFinal.add(SMSData("","2 hours ago",2))
        smsFinal.addAll(smsTwo)
        smsFinal.add(SMSData("","3 hours ago",3))
        smsFinal.addAll(smsThree)
        smsFinal.add(SMSData("","6 hours ago",6))
        smsFinal.addAll(smsSix)
        smsFinal.add(SMSData("","12 hours ago",12))
        smsFinal.addAll(smsTwelve)
        smsFinal.add(SMSData("","1 Day ago",24))
        smsFinal.addAll(smsDay)
        return smsFinal
    }
    fun Diff(date1:Date,date2:Date): Long {
        val mill_to_hour:Int=1000*60*60
        return (date1.time-date2.time)/mill_to_hour
    }
    fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_SMS)
        val result2 = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECEIVE_SMS)
        val result3 = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_CONTACTS)

        return result == PackageManager.PERMISSION_GRANTED&&result2== PackageManager.PERMISSION_GRANTED&&result3==PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(int: Int) {
        ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_CONTACTS), int)

    }
    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {

                val readSms = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val recieveSms = grantResults[1] == PackageManager.PERMISSION_GRANTED
                val readcontact = grantResults[2] == PackageManager.PERMISSION_GRANTED


                if (readSms&&readcontact&&recieveSms) {

                    Log.d("perm grant", "Permission Granted")
                } else {

                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)&&shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)&&shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                            showMessageOKCancel("Please allow access to both the permissions",
                                    object : DialogInterface.OnClickListener {
                                        override fun onClick(dialog: DialogInterface, which: Int) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(arrayOf<String>(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,Manifest.permission.READ_CONTACTS),
                                                        PERMISSION_REQUEST_CODE)
                                            }
                                        }
                                    })
                            return
                        }
                    }

                }
            }
        }
    }

    fun getContactName(phoneNumber: String, context: Context): String {
        val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))

        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)

        var contactName = ""
        val cursor = context.getContentResolver().query(uri, projection, null, null, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0)
            }
            cursor.close()
        }

        return contactName
    }
    override fun onResume() {
        super.onResume()
        if(!checkPermission())
        {
            requestPermission(PERMISSION_REQUEST_CODE)
        }
        else{
//            registerReceiver(broadCastReceiver,  IntentFilter("call_method"));

            val rightNow = Calendar.getInstance()
            val hourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY) // return the hour in 24 hrs format (ranging from 0-23)
            val minutes = rightNow.get(Calendar.MINUTE)

            val s: List<SMSData>
            s=readAllMessage()
            val messagecsv = ArrayList<SMSData>()

            for (i in s.indices) {
                messagecsv.add(s[i])
            }
            Log.d("list",messagecsv.toString())
            val smsAdapter = SmsAdapter( messagecsv,this)
            lv1.adapter = smsAdapter
        }


    }
    val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            val action_name = intent!!.getAction()
            if (action_name == "call_method") {
                Toast.makeText(this@MainActivity,"fgfh",Toast.LENGTH_SHORT).show()
            }

        }
    }

            fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(applicationContext)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show()
    }
    companion object {

        private val PERMISSION_REQUEST_CODE = 123
    }

    override fun onPause() {
        super.onPause()
    }
}


