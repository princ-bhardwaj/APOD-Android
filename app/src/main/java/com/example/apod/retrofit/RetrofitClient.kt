package com.example.apod.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val BASE_SERVER = "https://api.nasa.gov/"

    val retrofitClient: Retrofit.Builder by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_SERVER)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setLenient().create()
                )
            )
    }

    val apiInterface: ApiInterface by lazy {
        retrofitClient
            .build()
            .create(ApiInterface::class.java)
    }
}