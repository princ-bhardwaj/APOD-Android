package com.example.apod.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.apod.room.entity.PODImageData

@Dao
interface DAOAccess {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPODData(podData: PODImageData)

    @Query("SELECT * FROM POD WHERE date = :date")
    fun getPODData(date: String): PODImageData

    @Query("SELECT * FROM POD LIMIT 1")
    fun getLastPODData(): PODImageData

    @Query("DELETE FROM POD")
    fun deleteAll()
}