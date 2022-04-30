package com.project.geocodingapi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LocationEntity(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) : Parcelable