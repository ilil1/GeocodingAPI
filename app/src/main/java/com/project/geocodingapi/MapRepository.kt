package com.project.geocodingapi

import com.project.geocodingapi.LocationEntity


interface MapRepository {
    suspend fun getReverseGeoInformation(
        locationLatLngEntity: LocationEntity
    ): AddressInfo?
}