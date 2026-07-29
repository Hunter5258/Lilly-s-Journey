package com.lillyjourney.data.db

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.format(DateTimeFormatter.ISO_LOCAL_DATE)

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? =
        value?.let { LocalDate.parse(it, DateTimeFormatter.ISO_LOCAL_DATE) }

    @TypeConverter
    fun fromLocalDateTime(dt: LocalDateTime?): String? = dt?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? =
        value?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }

    @TypeConverter
    fun fromLocalTimeList(times: List<LocalTime>?): String? =
        times?.joinToString(",") { it.format(DateTimeFormatter.ofPattern("HH:mm")) }

    @TypeConverter
    fun toLocalTimeList(value: String?): List<LocalTime>? =
        value?.split(",")?.map {
            LocalTime.parse(it, DateTimeFormatter.ofPattern("HH:mm"))
        }

    @TypeConverter
    fun fromStringList(list: List<String>?): String? = list?.joinToString(";;")

    @TypeConverter
    fun toStringList(value: String?): List<String>? = value?.split(";;")?.filter { it.isNotBlank() }
}
