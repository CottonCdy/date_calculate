package com.example.datecalculate

import android.R
import android.app.*
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.sql.Time
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

class NotificationForegroundService : Service() {
    private var START_STATE = 1
    private var STOP_STATE = 0
    private var ERROR_STATE = -1
    private var message: String? = ""
    private var mThread: Thread? = null
    lateinit var preferences: SharedPreferences
    lateinit var endTime: LocalDateTime
    lateinit var timer: Timer

    var endTimeCountDownTimer: CountDownTimer? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChanel()
        preferences = getSharedPreferences("data_save", MODE_PRIVATE)
        endTime = initData()
        mThread.run {
            startNotification()
        }
        Log.d(TAG, "onCreate")
    }

    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        endTime = initData()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initData(): LocalDateTime {
        val selectYear = preferences.getInt("year", -1)
        val selectMonth = preferences.getInt("month", -1)
        val selectDay = preferences.getInt("day", -1)
        val selectHour = preferences.getInt("hour", -1)
        val selectMinute = preferences.getInt("minute", -1)
        val selectSecond = preferences.getInt("second", -1)

        val time =
            LocalDateTime.of(
                selectYear,
                selectMonth,
                selectDay,
                selectHour,
                selectMinute,
                selectSecond
            )

        return time
    }

    private fun startNotification() {
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val format = preferences.getInt("format", 0)
                val state = preferences.getInt("state", ERROR_STATE)

                // 시작 버튼을 눌렀을 경우
                if (state == START_STATE) message =
                    DateDifference().getEndTime(endTime, format).toString()
                // 중지 버튼을 눌렀을 경우
                else message = "stop"

                createNotification(message!!)

                Log.d("testtest", message!!)
            }
        }, 0, 1000)
    }

    private fun createNotification(msg: String) {
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.action = Intent.ACTION_MAIN
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getActivity(
            this,
            NOTI_ID,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this, "default")
            .setSmallIcon(R.drawable.checkbox_on_background)
            .setContentTitle("남은 시간 : " + msg)
            .setColor(Color.CYAN)
            .setContentIntent(pendingIntent) // 알림 클릭 시 이동

        // 알림 표시
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(NOTI_ID, builder.build()) // id : 정의해야하는 각 알림의 고유한 int값
        startForeground(NOTI_ID, builder.build())
    }

    private fun createNotificationChanel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "상단바 알림 고정"
            val channel =
                NotificationChannel("default", name, NotificationManager.IMPORTANCE_LOW)

            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        timer.cancel()
        stopSelf()

        if (mThread != null) {
            mThread!!.interrupt()
            mThread = null
        }

        Log.d(TAG, "onDestroy")
    }

    companion object {
        private const val TAG = "MyServiceTag"

        // Notification
        private const val NOTI_ID = 1
    }
}