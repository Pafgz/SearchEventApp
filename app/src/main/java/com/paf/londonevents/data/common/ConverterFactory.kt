package com.paf.londonevents.data.common

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class ConverterFactory : Converter.Factory() {

        override fun responseBodyConverter(type: Type?, annotations: Array<Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
            return if (String::class.java == type) {
                Converter<ResponseBody, Any> { it.string() }
            } else null
        }
}