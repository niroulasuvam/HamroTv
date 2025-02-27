package com.example.hamrotv.model

import android.os.Parcel
import android.os.Parcelable

data class MovieModel(
    var MovieId: String = "",
    var MovieName : String = "",
    var description: String = "",
    var Rating: Int =0,
    var imageUrl: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(MovieId)
        parcel.writeString(MovieName)
        parcel.writeString(description)
        parcel.writeInt(Rating)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieModel> {  // Changed to ExerciseModel
        override fun createFromParcel(parcel: Parcel): MovieModel {  // Changed return type
            return MovieModel(parcel)  // Changed constructor call
        }

        override fun newArray(size: Int): Array<MovieModel?> {  // Changed return type
            return arrayOfNulls(size)
        }
    }
}

