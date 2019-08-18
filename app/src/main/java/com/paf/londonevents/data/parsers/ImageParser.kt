package com.paf.londonevents.data.parsers

import android.net.Uri
import org.json.JSONArray
import org.json.JSONObject
import com.paf.londonevents.data.common.*

class ImageParser: JSONParser<Uri?> {

    private val MIN_WIDTH = 600
    private val MAX_WIDTH = 700

    override fun parse(json: JSON): Uri? {
        val images: JSONArray? = json("images")
        var imageUri: Uri? = null

        if (images != null && images.length() > 0){

            for (i in 0 until images.length()) {

                val imageJson = JSON(images[i] as JSONObject)

                if(isValidImage(imageJson)){
                    imageUri = parseImage(imageJson)
                    break
                }
            }

            if(imageUri == null){
                imageUri = parseImage(JSON(images[4] as JSONObject))
            }
        }

        return imageUri
    }

    private fun parseImage(json: JSON): Uri? {

        val url: String? = json("url")
        return Uri.parse(url)
    }


    private fun isValidImage(imageJson: JSON): Boolean {
        var isRatioGood = false
        val ratio: String? = imageJson("ratio")
        val width: Int? = imageJson("width")

        width?.let {
            if(ratio == "3_2" && width > MIN_WIDTH && width < MAX_WIDTH){
                isRatioGood = true
            }
        }

        return isRatioGood
    }


}
