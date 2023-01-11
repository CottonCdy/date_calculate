package com.example.datecalculate

import java.time.format.DateTimeFormatter

class DateFormat {

    val formatterYear = DateTimeFormatter.ofPattern("yyyy년")
    val formatterMonth = DateTimeFormatter.ofPattern("MM개월")
    val formatterDay = DateTimeFormatter.ofPattern("dd일")
    val formatterHour = DateTimeFormatter.ofPattern("HH시")
    val formatterMinute = DateTimeFormatter.ofPattern("mm분")
    val formatterSecond = DateTimeFormatter.ofPattern("ss초")

    val formatterYear1 = DateTimeFormatter.ofPattern("yyyy년")
    val formatterYear2 = DateTimeFormatter.ofPattern("yyyy년 MM월")
    val formatterYear3 = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
    val formatterYear4 = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시")
    val formatterYear5 = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")
    val formatterYear6 = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초")

    val formatterMonth1 = DateTimeFormatter.ofPattern("MM월")
    val formatterMonth2 = DateTimeFormatter.ofPattern("MM월 dd일")
    val formatterMonth3 = DateTimeFormatter.ofPattern("MM월 dd일 HH시")
    val formatterMonth4 = DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분")
    val formatterMonth5 = DateTimeFormatter.ofPattern("MM월 dd일 HH시 mm분 ss초")

    val formatterDay1 = DateTimeFormatter.ofPattern("dd일")
    val formatterDay2 = DateTimeFormatter.ofPattern("dd일 HH시")
    val formatterDay3 = DateTimeFormatter.ofPattern("dd일 HH시 mm분")
    val formatterDay4 = DateTimeFormatter.ofPattern("dd일 HH시 mm분 ss초")

    val formatterHour1 = DateTimeFormatter.ofPattern("HH시")
    val formatterHour2 = DateTimeFormatter.ofPattern("HH시 mm분")
    val formatterHour3 = DateTimeFormatter.ofPattern("HH시 mm분 ss초")

    val formatterMin1 = DateTimeFormatter.ofPattern("mm분")
    val formatterMin2 = DateTimeFormatter.ofPattern("mm분 ss초")

    val formatterSec = DateTimeFormatter.ofPattern("ss초")
}