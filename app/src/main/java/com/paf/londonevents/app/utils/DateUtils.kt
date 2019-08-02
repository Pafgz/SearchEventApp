package com.paf.londonevents.app.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun getDateFromString(string: String): Date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK).parse(string)
}

