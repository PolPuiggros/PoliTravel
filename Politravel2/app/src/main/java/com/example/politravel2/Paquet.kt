package com.example.politravel2

import android.os.Parcelable
import com.example.politravel.InterestPoint
import kotlinx.android.parcel.Parcelize

@Parcelize
class Paquet(
    val Id: Int,
    val Name_package: String,
    val Img: String,
    val Mean_of_transport: String,
    val Start_point: String,
    val Start_point_latitude: Float,
    val Start_point_longitude: Float,
    val Ending_point: String,
    val Ending_point_latitude: Float,
    val Ending_point_longitude: Float,
    val DurationOfTheTrip: Int,
    val Unit: Char,
    val Interest_points: ArrayList<InterestPoint>): Parcelable