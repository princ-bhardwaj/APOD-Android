package com.example.apod.repository

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.apod.BuildConfig
import com.example.apod.retrofit.RetrofitClient
import com.example.apod.room.ImageDatabase
import com.example.apod.room.entity.PODImageData
import com.example.apod.utils.ImageStorage
import com.example.apod.utils.Util
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MainRepository : LifecycleObserver {
    var podImageData = MutableLiveData<PODImageData>()
    var podDBData: PODImageData? = null

    fun getPictureOfTheDay(context: Context?): MutableLiveData<PODImageData> {
        getPODFromDB(context)
        if (podDBData != null) podImageData.value = podDBData
        else callPictureOfTheDayApi(context)
        return podImageData
    }

    private fun callPictureOfTheDayApi(context: Context?) {
        val call = RetrofitClient.apiInterface.getPictureOfTheDay(BuildConfig.API_KEY)
        call.enqueue(object : Callback<PODImageData> {
            override fun onFailure(call: Call<PODImageData>, t: Throwable) {
                podImageData.value = getLastPODFromDB(context)
            }

            override fun onResponse(
                call: Call<PODImageData>,
                response: Response<PODImageData>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val podResponse: PODImageData = response.body()!!
                    val observer = Observer<Uri?> { uri ->
                        deleteAll(context)
                        insertPODData(context, podResponse)
                        podImageData.value = podResponse
                    }
                    ImageStorage.saveToSdCard(
                        context!!,
                        podResponse.url!!,
                        ImageStorage.POD_FILENAME
                    ).observeForever(observer)
                } else podImageData.value = null
            }
        })
    }

    var imageDatabase: ImageDatabase? = null

    fun initializeDB(context: Context?): ImageDatabase {
        return ImageDatabase.getDataseClient(context)
    }

    private fun insertPODData(context: Context?, podData: PODImageData) {
        imageDatabase = initializeDB(context)
        CoroutineScope(IO).launch {
            imageDatabase!!.imageDao().insertPODData(podData)
        }
    }

    private fun deleteAll(context: Context?) {
        imageDatabase = initializeDB(context)
        CoroutineScope(IO).launch {
            imageDatabase!!.imageDao().deleteAll()
        }
    }

    private fun getPODFromDB(context: Context?): PODImageData? {
        imageDatabase = initializeDB(context)
        podDBData = imageDatabase!!.imageDao().getPODData(Util.getCurrentDate("yyyy-MM-dd"))
        return podDBData
    }

    private fun getLastPODFromDB(context: Context?): PODImageData? {
        imageDatabase = initializeDB(context)
        podDBData = imageDatabase!!.imageDao().getLastPODData()
        return podDBData
    }
}