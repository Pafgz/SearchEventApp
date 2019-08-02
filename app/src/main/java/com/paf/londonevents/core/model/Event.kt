package com.paf.londonevents.core.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName = "event")
data class Event(
    @ColumnInfo(name = "name") var name: String?,
    @ColumnInfo(name = "venue_name") var venueName: String?,
    @ColumnInfo(name = "image") val imageUri: Uri?,
    @ColumnInfo(name = "start_date") var startDateTime: Date?,
    @ColumnInfo(name = "is_favorite") var isFavorite: Boolean = false,
    @PrimaryKey @NotNull var id: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Uri::class.java.classLoader),
        Date(parcel.readLong()),
        parcel.readByte() != 0.toByte(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(venueName)
        parcel.writeParcelable(imageUri, flags)
        startDateTime?.time?.let { parcel.writeLong(it) }
        parcel.writeByte(if (isFavorite) 1 else 0)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}