package com.paf.londonevents

import android.net.Uri
import com.paf.londonevents.data.parsers.EventListParser
import com.paf.londonevents.data.parsers.ImageParser
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImageParserTest {

    private val json = getJsonFromJsonFile("SingleEventJson")

    @Test
    fun testParseEventListFromJson(){
        val expectedImage = Uri.parse("https://s1.ticketm.net/dam/a/10d/792ce874-3198-400b-9e69-f9ce8293a10d_1113381_RETINA_PORTRAIT_3_2.jpg")

        val actualList = ImageParser().parse(json)

        assertEquals(expectedImage, actualList)
    }
}