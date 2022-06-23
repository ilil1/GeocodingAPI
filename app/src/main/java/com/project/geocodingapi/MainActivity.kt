package com.project.geocodingapi

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.project.geocodingapi.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() , CoroutineScope {

    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener
    private lateinit var binding: ActivityMainBinding

    private lateinit var job: Job // Job : 코루틴의 상태확인 및 제어
    private lateinit var uiScope: CoroutineScope // 코루틴 생명주기 관리

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    companion object {
        val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermissions = permissions.entries.filter {
                it.key in locationPermissions //일치성 여부 확인.
            }

            if (responsePermissions.filter { it.value == true }.size == locationPermissions.size) {
                setLocationListener()
            } else {
                Toast.makeText(this, "no", Toast.LENGTH_SHORT).show()
            }
        }

    private fun getMylocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        // GPS의 권한여부
        val isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        //GPS가 허용이 되었다면
        if (isGpsEnable) {
            permissionLauncher.launch(locationPermissions)
        }
    }

    @Suppress("MissingPermission")
    private fun setLocationListener() {
        val minTime: Long = 1500
        val minDistance = 100f

        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }

        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, myLocationListener
            )

            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime, minDistance, myLocationListener
            )
        }
    }

    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            Toast.makeText(this@MainActivity, "${location.latitude}, ${location.longitude}", Toast.LENGTH_SHORT)
                .show()

            //binding.locationTitleTextView.text = "${location.latitude}, ${location.longitude}"

            getReverseGeoInformation( LocationEntity(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            )

            //binding.locationTitleTextView.text = mapSearchInfoEntity.fullAddress
            removeLocationListener()
        }

        private fun removeLocationListener() {
            if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
                locationManager.removeUpdates(myLocationListener)
            }
        }
    }

    fun getReverseGeoInformation(locationLatLngEntity: LocationEntity) {

        uiScope.launch {
            withContext(Dispatchers.Main) {

                val response = RetrofitUtil.mapApiService.getReverseGeoCode(
                    lat = locationLatLngEntity.latitude,
                    lon = locationLatLngEntity.longitude
                )

                if (response.isSuccessful) {
                    val body = response.body()
//                    withContext(Dispatchers.Main) {
//                        binding.locationTitleTextView.text = "${body?.addressInfo?.fullAddress}"
//                    }
                    binding.locationTitleTextView.text = "${body?.addressInfo?.fullAddress}"
                }
                else {
                    null
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()
        //uiScope = CoroutineScope(Dispatchers.Main+job)
        uiScope = CoroutineScope(Dispatchers.Main) // UI와 상호작용하거나 빠른 작업을 위해 메인스레드에서 코루틴 실행

        getMylocation()
    }
}