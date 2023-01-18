package com.example.datecalculate

import java.time.LocalDateTime
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit

class DateDifference {
    fun getEndTime(endTime: LocalDateTime, format: Int): String? {
        // 현재 시간
        val currentTime = LocalDateTime.now()

        // 총 시간 기준 일, 시, 분, 초
        val day = ChronoUnit.DAYS.between(currentTime, endTime) // 일
        val hour = ChronoUnit.HOURS.between(currentTime, endTime) // 시간
        val minute = ChronoUnit.MINUTES.between(currentTime, endTime) // 분
        val second = ChronoUnit.SECONDS.between(currentTime, endTime) // 초

        val subDate = endTime.minusYears(currentTime.getLong(ChronoField.YEAR))
            .minusMonths(currentTime.getLong(ChronoField.MONTH_OF_YEAR))
            .minusDays(currentTime.getLong(ChronoField.DAY_OF_MONTH))
            .minusHours(currentTime.getLong(ChronoField.HOUR_OF_DAY))
            .minusMinutes(currentTime.getLong(ChronoField.MINUTE_OF_HOUR))
            .minusSeconds(currentTime.getLong(ChronoField.SECOND_OF_MINUTE))
            .plusDays(1)


        // 분리된 연, 월, 일, 시, 분, 초
        var diffYear = subDate.year
        var diffMonth = subDate.monthValue
        var diffDay = subDate.dayOfMonth - 1
        var diffHour = subDate.hour
        var diffMinute = subDate.minute
        var diffSecond = subDate.second

        if (diffMonth == 12) {
            diffYear++
            diffMonth = 0
        }

        when (format) {
            // year +
            0 -> return "${diffYear}년 ${diffMonth}개월 ${diffDay}일 ${diffHour}시간 ${diffMinute}분 ${diffSecond}초 남았습니다."
            1 -> return "${diffYear}년 ${diffMonth}개월 ${diffDay}일 ${diffHour}시간 ${diffMinute}분 남았습니다."
            2 -> return "${diffYear}년 ${diffMonth}개월 ${diffDay}일 ${diffHour}시간 남았습니다."
            3 -> return "${diffYear}년 ${diffMonth}개월 ${diffDay}일 남았습니다."
            4 -> return "${diffYear}년 ${diffMonth}개월 남았습니다."
            5 -> return "${diffYear}년"

            // month +
            6 -> return "${diffMonth}개월 ${diffDay}일 ${diffHour}시간 ${diffMinute}분 ${diffSecond}초 남았습니다."
            7 -> return "${diffMonth}개월 ${diffDay}일 ${diffHour}시간 ${diffMinute}분 남았습니다."
            8 -> return "${diffMonth}개월 ${diffDay}일 ${diffHour}시간 남았습니다."
            9 -> return "${diffMonth}개월 ${diffDay}일 남았습니다."
            10 -> return "${diffMonth}개월 남았습니다."

            // day +
            11 -> return "${day}일 ${diffHour}시간 ${diffMinute}분 ${diffSecond}초 남았습니다."
            12 -> return "${day}일 ${diffHour}시간 ${diffMinute}분 남았습니다."
            13 -> return "${day}일 ${diffHour}시간 남았습니다."
            14 -> return "${day}일 남았습니다."

            // hour +
            15 -> return "${hour}시간 ${diffMinute}분 ${diffSecond}초 남았습니다."
            16 -> return "${hour}시간 ${diffMinute}분 남았습니다."
            17 -> return "${hour}시간 남았습니다."

            // minute +
            18 -> return "${minute}분 ${diffSecond}초 남았습니다."
            19 -> return "${minute}분 남았습니다."

            // second +
            20 -> return "${second}초 남았습니다."

            else -> return "[Error] 지원되지 않는 형식입니다."
        }
    }
}