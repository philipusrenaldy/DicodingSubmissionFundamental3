package com.submission.submission2

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReminderAlarm : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val title = TYPE_REPEATING
        val reminderId = ID_REPEATING
        val message = MESSAGE
        showNotification(context, title, reminderId, message)
    }

    fun setReminder(context: Context, type: String, time: String){
        if (dateInvalid(time, TIME_FORMAT)) return
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderAlarm::class.java)
        intent.putExtra(EXTRA_TYPE, type)

        val reminderTime = time.split(":").toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, Integer.parseInt(reminderTime[0]))
            set(Calendar.MINUTE, Integer.parseInt(reminderTime[1]))
            set(Calendar.SECOND, 0)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
        Toast.makeText(context,"Reminder on at $time AM", Toast.LENGTH_SHORT).show()
    }

    private fun dateInvalid(time: String, timeFormat: String): Boolean {
        return try {
            val format = SimpleDateFormat(timeFormat, Locale.getDefault())
            format.isLenient = true
            format.parse(time)
            false
        } catch (e: ParseException){
            true
        }
    }

    fun reminderOff(context: Context){
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderAlarm::class.java)
        val reqId = ID_REPEATING
        val pendingIntent = PendingIntent.getBroadcast(context, reqId, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "You Turn Off Reminder. For Best Experienced Please Turn On Again", Toast.LENGTH_LONG).show()
    }

    private fun showNotification(
        context: Context,
        title: String,
        reminderId: Int,
        message: String
    ) {
        val channelId = "Channel 1"
        val channelName = "Github Repeat Reminder"
        val notifyIntent = Intent(context, MainActivity::class.java)
        val notifyPendingIntent = PendingIntent.getActivity(context, ID_REPEATING, notifyIntent, 0)
        val notifyManagement = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_baseline_alarm_24)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setSound(sound)
            .setContentIntent(notifyPendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            builder.setChannelId(channelId)
            notifyManagement.createNotificationChannel(channel)
        }
        val notify = builder.build()
        notifyManagement.notify(reminderId,notify)
    }

    companion object{
        const val TYPE_REPEATING = "User Alarm"
        const val ID_REPEATING = 100
        const val EXTRA_TYPE = "EXTRA_TYPE"
        const val EXTRA_TIME = "09:00"
        const val MESSAGE = "Find People You Like at Github"
        private const val TIME_FORMAT = "HH:mm"
    }
}