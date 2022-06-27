package com.project.geocodingapi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitUtil {

    private var instance: Retrofit? = null

    val mapApiService: MapApiService by lazy {
        getRetrofit().create(MapApiService::class.java)
    }

    private fun getRetrofit(): Retrofit {
        if(instance == null) {
            Retrofit.Builder()
                .baseUrl(Url.TMAP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildOkHttpClient())
                .build()
        }
        return instance!!
    }

    private fun buildOkHttpClient(): OkHttpClient {

        val interceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }
}