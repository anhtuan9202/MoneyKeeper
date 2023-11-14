 package com.example.moneykeeper.presenter.setting.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.moneykeeper.R
import com.example.moneykeeper.databinding.FragmentNotificationBinding
import com.example.moneykeeper.presenter.base.BaseFragment
import com.example.moneykeeper.presenter.utils.PrefsUtils
import java.util.Calendar



 class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

     private  var notificationTime: Long = 0
     private lateinit var alarmManager: AlarmManager
     private lateinit var pendingIntent: PendingIntent
     override fun getLayout(container: ViewGroup?): FragmentNotificationBinding {
         return FragmentNotificationBinding.inflate(layoutInflater)
     }

     override fun initViews() {
         PrefsUtils.initialize(requireContext())
         binding.ivBack.setOnClickListener {
             callback.backToPrevious()
         }
         val c = Calendar.getInstance()
         c.set(Calendar.HOUR_OF_DAY, 20)
         c.set(Calendar.MINUTE, 0)
         c.set(Calendar.SECOND,0)
         notificationTime = c.timeInMillis



         alarmManager = requireContext().getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
         val intent = Intent(requireContext(),NotificationReceiver::class.java)
         pendingIntent = PendingIntent.getBroadcast(requireContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


         binding.tvNotificationTime.setOnClickListener {
            showTimePicker()
        }
         val time: String? = PrefsUtils.getString("notification_time")
         if (time != null) {
             binding.tvNotificationTime.text = time
         } else {
             binding.tvNotificationTime.text = "20:00"
             PrefsUtils.putString("notification_time", "20:00")
         }


         if(PrefsUtils.getBoolean("notification")){
             binding.swichNotifi.isChecked = true
             binding.tvNotificationTime.visibility = View.INVISIBLE
         }
         else {
             binding.swichNotifi.isChecked = false
             binding.tvNotificationTime.visibility = View.VISIBLE

         }
         binding.swichNotifi.setOnCheckedChangeListener { _, isChecked ->
             if (isChecked) {
                 alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, notificationTime,
                     AlarmManager.INTERVAL_DAY,pendingIntent)
                 notify(getString(R.string.set_notification))
                 Log.d("notifi",notificationTime.toString())
                 binding.tvNotificationTime.visibility = View.INVISIBLE
             } else {
                 alarmManager.cancel(pendingIntent)
                 notify(getString(R.string.unset_notifi))
                 binding.tvNotificationTime.visibility = View.VISIBLE
             }
             saveNotificationPreference(isChecked)

         }
     }


     private fun showTimePicker() {
         val c = Calendar.getInstance()
         val hour = c.get(Calendar.HOUR_OF_DAY)
         val minute = c.get(Calendar.MINUTE)

         val timePickerDialog = TimePickerDialog(
             requireContext(),
             TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                 val calendar = Calendar.getInstance()
                 calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                 calendar.set(Calendar.MINUTE, selectedMinute)
                 calendar.set(Calendar.SECOND,0)

                 val timeString = String.format("%02d:%02d", selectedHour, selectedMinute)
                 binding.tvNotificationTime.text = timeString
                 PrefsUtils.putString("notification_time",timeString)
                 notificationTime = calendar.timeInMillis
                 Log.d("notifi", "picker$notificationTime")


             },
             hour,
             minute,
             true
         )

         timePickerDialog.show()
     }

     private fun saveNotificationPreference(mode: Boolean) {
         PrefsUtils.putBoolean("notification",mode)
     }

 }