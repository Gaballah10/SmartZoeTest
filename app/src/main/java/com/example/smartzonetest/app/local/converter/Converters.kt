package com.example.smartzonetest.app.local.converter

import androidx.room.TypeConverter
import com.example.smartzonetest.network.responses.Source
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromSource(source: Source?): String? {
        return if (source == null) null else Gson().toJson(source)
    }

    @TypeConverter
    fun toSource(value: String?): Source? {
        return if (value == null)
            null
        else
            Gson().fromJson<Source>(value, object : TypeToken<Source>() {}.type)
    }

}
