package com.project.geocodingapi

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultMapRepository(
    private val mapApiService: MapApiService,
    private val ioDispatcher: CoroutineDispatcher
) : MapRepository {
    override suspend fun getReverseGeoInformation(locationLatLngEntity: LocationEntity): AddressInfo? {
        TODO("Not yet implemented")
    }
    //MainViewModel에서 실행한다.
//    override suspend fun getReverseGeoInformation(locationLatLngEntity: LocationEntity) =
//        withContext(ioDispatcher) {
//
//            val response = mapApiService.getReverseGeoCode(
//                lat = locationLatLngEntity.latitude,
//                lon = locationLatLngEntity.longitude
//            )
//            if (response.isSuccessful) {
//                Log.d("TAG", "DefaultMapRepository(0): ${response}")
//                Log.d("TAG", "DefaultMapRepository(1): ${response.body()}")
//                //Log.d("TAG", "DefaultMapRepository(2): ${response.body()?.addressInfo}")
//                //response.body()?.addressInfo
//                response.body()
//            } else {
//                null
//            }
//        }
}