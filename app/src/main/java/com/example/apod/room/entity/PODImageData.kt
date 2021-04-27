package com.example.apod.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "POD")
data class PODImageData(

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "explanation")
    var explanation: String,

    @ColumnInfo(name = "url")
    var url: String,

    @ColumnInfo(name = "title")
    var title: String

) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var Id: Int? = null

}