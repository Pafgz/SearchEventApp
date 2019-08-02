package com.paf.londonevents.data.common

fun Any.getRandomString(): String = (0..8).random().toString() + (0..9).random().toString()