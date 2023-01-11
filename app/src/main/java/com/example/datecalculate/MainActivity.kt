package com.example.datecalculate

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import java.text.SimpleDateFormat
import java.util.*

import android.os.CountDownTimer
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import kotlin.math.floor


class MainActivity : AppCompatActivity() {
    var format = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 현재 시간을 나타내는 text
        val currentDateText = findViewById<TextView>(R.id.current)

        // 선택한 시간을 나타내는 text
        val selectDateText = findViewById<TextView>(R.id.select_date)

        // 남은 시간(결과)을 나타내는 text
        val resultDateText = findViewById<TextView>(R.id.result_time)

        // 스피너 설정 (날짜 포맷형식 선택)
        val spinner = findViewById<AppCompatSpinner>(R.id.spinner_dateFormat)
        ArrayAdapter.createFromResource(
            this,
            R.array.array_list,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                p1: View?,
                position: Int,
                p3: Long
            ) {
                if (position == 0) format = 0
                else if (position == 1) format = 1
                else if (position == 2) format = 2
                else if (position == 3) format = 3
                else if (position == 4) format = 4
                else if (position == 5) format = 5
                else if (position == 6) format = 6
                else if (position == 7) format = 7
                else if (position == 8) format = 8
                else if (position == 9) format = 9
                else if (position == 10) format = 10

                //resultDateText.text =
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                resultDateText.text = "선택 없음"
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

        val endTime = LocalDateTime.of(2023, 2, 11, 4, 47, 19)
        selectDateText.text = endTime.format(DateFormat().formatterYear6)

        val endTimer: Long =
            ChronoUnit.SECONDS.between(LocalDateTime.now(), endTime) * 1000 // 남은 시간동안 카운트
        val endTimeCountDownTimer = object : CountDownTimer(endTimer, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                resultDateText.setText(getEndTime(endTime, format))
            }

            override fun onFinish() {
                resultDateText.setText("설정한 시간이 경과하였습니다.")
            }
        }

        currentTimeCountDownTimer.start()
        endTimeCountDownTimer.start()
    }

    private fun getCurrentTime(): String? {
        // 현재 시간
        val currentTime = LocalDateTime.now()
        val formatter = DateFormat()

        // 형식 : 년 월 일 시간 분 초
        return currentTime.format(formatter.formatterYear6)
    }

    private fun getEndTime(endTime: LocalDateTime, format: Int): String? {
        // 현재 시간
        val currentTime = LocalDateTime.now()
        val formatter = DateFormat()

        val year = ChronoUnit.YEARS.between(currentTime, endTime) // 년
        val month = ChronoUnit.MONTHS.between(currentTime, endTime) // 월
        val day = ChronoUnit.DAYS.between(currentTime, endTime) // 일
        val hour = ChronoUnit.HOURS.between(currentTime, endTime) // 시간
        val minute = ChronoUnit.MINUTES.between(currentTime, endTime) // 분
        val second = ChronoUnit.SECONDS.between(currentTime, endTime) // 초

        val e = currentTime.get(ChronoField.DAY_OF_MONTH)
        // Log.d("testtest", e.toString())

        val diffDate = endTime.minusYears(currentTime.getLong(ChronoField.YEAR))
            .minusMonths(currentTime.getLong(ChronoField.MONTH_OF_YEAR))
            .minusDays(currentTime.getLong(ChronoField.DAY_OF_MONTH))
            .minusHours(currentTime.getLong(ChronoField.HOUR_OF_DAY))
            .minusMinutes(currentTime.getLong(ChronoField.MINUTE_OF_HOUR))
            .minusSeconds(currentTime.getLong(ChronoField.SECOND_OF_MINUTE))
            .truncatedTo(ChronoUnit.SECONDS)
            .format(formatter.formatterYear6)

        Log.d("testtest", diffDate.toString())

        /*
        val diffYear = endTime.minusYears(currentTime.getLong(ChronoField.YEAR)).year

        val diffMonth =
            endTime.minusMonths(currentTime.getLong(ChronoField.MONTH_OF_YEAR)).monthValue // 문제
        val diffDay = endTime.minusDays(currentTime.getLong(ChronoField.DAY_OF_MONTH)).dayOfMonth // 문제

        val diffHour = endTime.minusHours(currentTime.getLong(ChronoField.HOUR_OF_DAY)).hour
        val diffMinute =
            endTime.minusMinutes(currentTime.getLong(ChronoField.MINUTE_OF_HOUR)).minute
        val diffSecond =
            endTime.minusSeconds(currentTime.getLong(ChronoField.SECOND_OF_MINUTE)).second

         */


/*
        val diffYear = dateDiv.format(formatter.formatterYear)
        val diffMonth = dateDiv.format(formatter.formatterMonth)
        val diffDay = dateDiv.format(formatter.formatterDay)
        val diffHour = dateDiv.format(formatter.formatterHour)
        val diffMinute = dateDiv.format(formatter.formatterMinute)
        val diffSecond = dateDiv.format(formatter.formatterSecond)
*/
        /*
        when (format) {
            0 -> return "${diffYear}년"
            1 -> return "${diffYear}년 ${diffMonth}개월 남았습니다."
            2 -> return "${diffYear}년 ${diffMonth}개월 ${diffDay}일 남았습니다."
            3 -> return "${diffYear}년 ${diffMonth}개월 ${diffDay}일 ${diffHour}시간 남았습니다."
            4 -> return "${diffYear}년 ${diffMonth}개월 ${diffDay}일 ${diffHour}시간 ${diffMinute}분 남았습니다."
            5 -> return "${diffYear}년 ${diffMonth}개월 ${diffDay}일 ${diffHour}시간 ${diffMinute}분 ${diffSecond}초 남았습니다."

            6 -> return "${month}개월 남았습니다."
            7 -> return "${month}개월 ${month}일 남았습니다."
            8 -> return "${month}개월 ${month}일 ${hour % 24}시간 남았습니다."
            9 -> return "${month}개월 ${month}일 ${hour % 24}시간 ${minute % 60}분 남았습니다."
            10 -> return "${month}개월 ${month}일 ${hour % 24}시간 ${minute % 60}분 ${second % 60}초 남았습니다."

            else -> return "[Error] 지원되지 않는 형식입니다."
        }

         */
        return "0"
    }
}