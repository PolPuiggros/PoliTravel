package com.example.politravel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable


class InterestPoint(val Interest_point_name: String, val Interest_point_latitude: Float,val Interest_point_longitude: Float ): Serializable
{
    override fun toString(): String {
        return Interest_point_name
    }
}