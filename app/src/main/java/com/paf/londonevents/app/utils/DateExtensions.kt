package com.paf.londonevents.app.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.getString(): String = SimpleDateFormat("yyyy-MM-dd", Locale.UK).format(this)

