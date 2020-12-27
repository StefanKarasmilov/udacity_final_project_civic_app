package com.example.android.politicalpreparedness.network.jsonadapter

import android.annotation.SuppressLint
import com.squareup.moshi.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class LocalDateTimeAdapter : JsonAdapter<Date>() {

    companion object {
        const val SERVER_FORMAT = ("yyyy-MM-dd'T'HH:mm") // define your server format here
    }

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat(SERVER_FORMAT)

    @FromJson
    override fun fromJson(reader: JsonReader): Date? {
        return try {
            val dateAsString = reader.nextString()
            dateFormat.parse(dateAsString)
        } catch (e: Exception) {
            null
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Date?) {
        if (value != null) {
            writer.value(value.toString())
        }
    }


}