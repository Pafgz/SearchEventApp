package com.paf.londonevents.data.events

import com.paf.londonevents.app.utils.DateUtils
import java.util.*

class EventSearchQueryParams {
    var eventTitle: String? = null
    val city: String = "london"
    val local: String = "en-gb"
    val pageSize: Int = 10
    val sort: String = "date,asc"
    val startDateTime: Date = DateUtils.getCurrentDateTime()
}