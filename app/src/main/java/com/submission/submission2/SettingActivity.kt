package com.submission.submission2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.submission.submission2.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var reminderAlarm: ReminderAlarm
    private lateinit var bind: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(bind.root)
        supportActionBar?.hide()

        reminderAlarm = ReminderAlarm()
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
        val getBoolean = sharedPreferences.getBoolean(BOOLEAN_KEY, false)
        bind.alarmSwitch.isChecked = getBoolean
        bind.alarmSwitch.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                val edit = sharedPreferences.edit()
                edit.apply{
                    putBoolean(BOOLEAN_KEY, true)
                }.apply()
                reminderAlarm.setReminder(this,ReminderAlarm.EXTRA_TYPE, ReminderAlarm.EXTRA_TIME)
            }
            else{
                val edit = sharedPreferences.edit()
                edit.apply{
                    putBoolean(BOOLEAN_KEY, false)
                }.apply()
                reminderAlarm.reminderOff(this)
            }
        }
    }

    companion object{
        const val SHARED_PREFERENCE = "sharedPreference"
        const val BOOLEAN_KEY = "booleanKey"
    }
}