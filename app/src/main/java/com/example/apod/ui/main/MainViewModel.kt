package com.example.apod.ui.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apod.repository.MainRepository
import com.example.apod.room.entity.PODImageData

class MainViewModel : ViewModel() {
    var pictureOfTheDayLiveData: MutableLiveData<PODImageData>? = null

    fun getPictureOfTheDay(context: Context?): LiveData<PODImageData>? {
        pictureOfTheDayLiveData = MainRepository.getPictureOfTheDay(context)
        return pictureOfTheDayLiveData
    }
}