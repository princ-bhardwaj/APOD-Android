package com.example.apod.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.apod.room.entity.PODImageData


@Database(entities = arrayOf(PODImageData::class), version = 1, exportSchema = false)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun imageDao(): DAOAccess

    companion object {

        @Volatile
        private var INSTANCE: ImageDatabase? = null

        fun getDataseClient(context: Context?): ImageDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context!!, ImageDatabase::class.java, "POD_DATABASE")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()

                return INSTANCE!!

            }
        }

    }
}