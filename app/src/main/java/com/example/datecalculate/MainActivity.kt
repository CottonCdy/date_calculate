package com.example.datecalculate

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatSpinner
import com.ikovac.timepickerwithseconds.MyTimePickerDialog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


class MainActivity : AppCompatActivity() {
    private var START_STATE = 1
    private var STOP_STATE = 0
    private var INIT_STATE = -1
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var format = 0
    var selectYear = -1
    var selectMonth = -1
    var selectDay = -1
    var selectHour = -1
    var selectMinute = -1
    var selectSecond = -1
    var startClickCheck = INIT_STATE
    var endTimeCountDownTimer: CountDownTimer? = null
    lateinit var dateData: String
    lateinit var timeData: String

    lateinit var currentDateText: TextView // 현재 시간을 나타내는 text
    lateinit var selectDateText: TextView  // 선택한 시간을 나타내는 text
    lateinit var resultDateText: TextView // 남은 시간(결과)을 나타내는 text
    lateinit var dateSelect: AppCompatButton
    lateinit var timeSelect: AppCompatButton
    lateinit var startButton: AppCompatButton
    lateinit var pushCheck: AppCompatCheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferences = getSharedPreferences("data_save", Context.MODE_PRIVATE)
        editor = preferences.edit()

        format = preferences.getInt("format", 0)
        selectYear = preferences.getInt("year", -1)
        selectMonth = preferences.getInt("month", -1)
        selectDay = preferences.getInt("day", -1)
        selectHour = preferences.getInt("hour", -1)
        selectMinute = preferences.getInt("minute", -1)
        selectSecond = preferences.getInt("second", -1)
        startClickCheck = preferences.getInt("click_check", INIT_STATE)
        dateData = preferences.getString("date_data", "-").toString()
        timeData = preferences.getString("time_data", "").toString()

        currentDateText = findViewById<TextView>(R.id.current) // 현재 시간을 나타내는 text
        selectDateText = findViewById<TextView>(R.id.select_date) // 선택한 시간을 나타내는 text
        resultDateText = findViewById<TextView>(R.id.result_time) // 남은 시간(결과)을 나타내는 text
        dateSelect = findViewById<AppCompatButton>(R.id.date_select)
        timeSelect = findViewById<AppCompatButton>(R.id.time_select)
        startButton = findViewById<AppCompatButton>(R.id.start)
        pushCheck = findViewById<AppCompatCheckBox>(R.id.create_notification)

        selectDateText.text = dateData + timeData

        // 날짜 선택 버튼
        dateSelect.setOnClickListener {
            val cal = Calendar.getInstance()
            val resDate = cal.apply {
                set(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DATE)
                )
            }

            val datePicker =
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->
                    selectYear = y
                    selectMonth = m + 1
                    selectDay = d
                    dateData = "${selectYear}년 ${selectMonth}월 ${selectDay}일 "
                    selectDateText.text = dateData + timeData
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).apply {
                    datePicker.minDate = resDate.timeInMillis
                }
            datePicker.show()
        }

        // 시간 선택 버튼
        timeSelect.setOnClickListener {
            val cal = Calendar.getInstance()
            MyTimePickerDialog(
                this,
                { view, h, mm, s ->
                    selectHour = h
                    selectMinute = mm
                    selectSecond = s
                    timeData = "${selectHour}시 ${selectMinute}분 ${selectSecond}초"
                    selectDateText.text = dateData + timeData
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                true
            ).show()
        }

        val serviceIntent = Intent(this@MainActivity, NotificationForegroundService::class.java)

        // 만약 시간을 설정하지 않았다면
        if ((selectYear == -1) || (selectMonth == -1) || (selectDay == -1)
            || (selectHour == -1) || (selectMinute == -1) || (selectSecond == -1)
        ) {
            pushCheck.isEnabled = false
        }

        pushCheck.setOnClickListener {
            // 초기 상태인 경우
            /*
            if (startClickCheck == INIT_STATE) {
                Toast.makeText(applicationContext, "123", Toast.LENGTH_SHORT)
                    .show()
                pushCheck.isChecked = false
                pushCheck.isEnabled = false
            }*/

            if (pushCheck.isChecked) startService(serviceIntent)
            else stopService(serviceIntent)

            Log.d("testtest", "push : " + pushCheck.isChecked.toString())
            editor.putBoolean("push_check", pushCheck.isChecked)
            editor.apply()
        }

        pushCheck.isChecked = preferences.getBoolean("push_check", false)
        if (pushCheck.isChecked) startService(serviceIntent)
        else stopService(serviceIntent)

        Log.d("testtest", "push : " + pushCheck.isChecked.toString())

        startButton.setOnClickListener {
            if (startClickCheck == STOP_STATE || startClickCheck == INIT_STATE) start(serviceIntent)
            else stop()

            editor.putInt("click_check", startClickCheck)
            editor.apply()
        }

        Log.d("testtest", startClickCheck.toString())
        // 앱을 다시 켤 때 상태 값 받아와서 실행
        if (startClickCheck == START_STATE) start(serviceIntent) // 시작 상태
        else if (startClickCheck == STOP_STATE) stop() // 정지 상태
        else init() // 초기 상태

        // 스피너 설정 (날짜 포맷형식 선택)
        val spinner = findViewById<AppCompatSpinner>(R.id.spinner_dateFormat)
        ArrayAdapter.createFromResource(
            this,
            R.array.array_list,
            R.layout.spinner_dropdown_item
        ).also { adapter ->
            spinner.adapter = adapter
            spinner.viewTreeObserver.addOnGlobalLayoutListener {
                (spinner.selectedView as TextView).setTextColor(Color.WHITE)
                (spinner.selectedView as TextView).setTypeface(null, Typeface.BOLD_ITALIC)
                (spinner.selectedView as TextView).paintFlags = Paint.UNDERLINE_TEXT_FLAG
                (spinner.selectedView as TextView).setBackgroundResource(R.drawable.spinner_custom)
            }
        }

        spinner.setSelection(format)
        spinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    when (position) {
                        // year +
                        0 -> format = 0
                        1 -> format = 1
                        2 -> format = 2
                        3 -> format = 3
                        4 -> format = 4
                        5 -> format = 5

                        // month +
                        6 -> format = 6
                        7 -> format = 7
                        8 -> format = 8
                        9 -> format = 9
                        10 -> format = 10

                        // day +
                        11 -> format = 11
                        12 -> format = 12
                        13 -> format = 13
                        14 -> format = 14

                        // hour +
                        15 -> format = 15
                        16 -> format = 16
                        17 -> format = 17

                        // minute +
                        18 -> format = 18
                        19 -> format = 19

                        // second
                        20 -> format = 20
                    }

                    editor.putInt("format", format)
                    editor.apply()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    resultDateText.text = "-"
                }
            }

        // millisInFuture : 얼마나 작동할 것인지
        // countDownInterval : 몇 초 간격으로 작동할 것인지
        val currentTimer: Long = System.currentTimeMillis() // Todo : 정확한 시간 인자값 전달
        val currentTimeCountDownTimer = object : CountDownTimer(currentTimer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                currentDateText.setText(getCurrentTime())
            }

            override fun onFinish() {}
        }

        currentTimeCountDownTimer.start()
    }

    private fun init() {
        resultDateText.text = "[END]\n" + "설정한 시간이 경과했습니다."
        // pushCheck.isEnabled = false
    }

    private fun start(serviceIntent: Intent) {
        editor.putString("date_data", dateData)
        editor.putString("time_data", timeData)
        editor.putInt("year", selectYear)
        editor.putInt("month", selectMonth)
        editor.putInt("day", selectDay)
        editor.putInt("hour", selectHour)
        editor.putInt("minute", selectMinute)
        editor.putInt("second", selectSecond)
        editor.putInt("state", START_STATE)
        editor.apply()

        // 알림 체크박스 활성화
        // pushCheck.isEnabled = true

        if (pushCheck.isChecked) startService(serviceIntent)
        else stopService(serviceIntent)

        if (endTimeCountDownTimer != null)
            endTimeCountDownTimer?.cancel()

        // 만약 시간을 설정하지 않았다면
        if ((selectYear == -1) || (selectMonth == -1) || (selectDay == -1)
            || (selectHour == -1) || (selectMinute == -1) || (selectSecond == -1)
        ) {
            Toast.makeText(applicationContext, "목표 시간을 설정해 주세요.", Toast.LENGTH_SHORT)
                .show()
            return
        }

        pushCheck.isEnabled = true

        // 최종 목표 시간
        val endTime =
            LocalDateTime.of(
                selectYear,
                selectMonth,
                selectDay,
                selectHour,
                selectMinute,
                selectSecond
            )

        selectDateText.text =
            endTime.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 H시 m분 s초"))

        val endTimer: Long =
            ChronoUnit.MILLIS.between(LocalDateTime.now(), endTime) + 1000 // 남은 시간동안 카운트
        endTimeCountDownTimer = object : CountDownTimer(endTimer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                resultDateText.setText(DateDifference().getEndTime(endTime, format))
            }

            override fun onFinish() {
                Toast.makeText(applicationContext, "설정한 시간이 경과하였습니다.", Toast.LENGTH_SHORT)
                    .show()
                stop(INIT_STATE)
            }
        }

        startButton.text = "D-DAY 중지"
        startButton.setBackgroundResource(R.drawable.button_cancel_shape)
        dateSelect.isClickable = false
        dateSelect.setBackgroundResource(R.drawable.button_cancel_shape)
        timeSelect.isClickable = false
        timeSelect.setBackgroundResource(R.drawable.button_cancel_shape)

        startClickCheck = START_STATE
        endTimeCountDownTimer?.start()
    }

    private fun stop(state: Int = STOP_STATE) {
        editor.putInt("state", state)
        editor.apply()

        startButton.text = "D-DAY 시작"
        startButton.setBackgroundResource(R.drawable.button_shape)
        dateSelect.isClickable = true
        dateSelect.setBackgroundResource(R.drawable.button_shape)
        timeSelect.isClickable = true
        timeSelect.setBackgroundResource(R.drawable.button_shape)

        startClickCheck = state

        if (state == STOP_STATE) {
            resultDateText.text = "[stop]\n" + "시작 버튼을 눌러주세요"
            //pushCheck.isEnabled = true
        } else {
            resultDateText.text = "[END]\n" + "설정한 시간이 경과했습니다."
            //pushCheck.isEnabled = false
        }

        endTimeCountDownTimer?.cancel()
    }

    private fun getCurrentTime(): String? {
        // 현재 시간
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 H시 m분 s초")

        // 형식 : 년 월 일 시간 분 초
        return currentTime.format(formatter)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}