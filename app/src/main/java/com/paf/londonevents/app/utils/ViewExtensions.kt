package com.paf.londonevents.app.utils

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setImage(imageUri: Uri) {
    Glide.with(this).load(imageUri).into(this)
}

fun ImageView.setImage(drawableId: Int) {
    setImageDrawable(resources.getDrawable(drawableId, null))
}