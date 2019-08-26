package com.paf.londonevents.data.parsers

import com.paf.londonevents.data.common.JSON
import com.paf.londonevents.data.common.invoke
import com.paf.londonevents.data.common.JSONParser

class EventNextPageParser: JSONParser<Int> {

    override fun parse(json: JSON): Int {
        var nextPageNumber: Int = json("page.number") ?: -1
        if(nextPageNumber > -1) nextPageNumber++
        return nextPageNumber
    }

}