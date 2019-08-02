package com.paf.londonevents

import com.paf.londonevents.data.common.JSON
import java.io.File

fun Any.getJsonFromJsonFile(name: String): JSON {
    val jsonString = getJsonStringFromJsonFile(name)
    return JSON(jsonString)
}

fun Any.getJsonStringFromJsonFile(name: String): String {

    val path = javaClass.classLoader!!.getResource(name)
    val file = File(path.toURI())
    return file.readText()
}