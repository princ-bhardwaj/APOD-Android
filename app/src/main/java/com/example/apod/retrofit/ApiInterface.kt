package com.example.apod.retrofit

import com.example.apod.room.entity.PODImageData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface{

    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Call<PODImageData>
}