package com.example.test.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Video")
data class Video(
    @PrimaryKey(autoGenerate = true)
    var videoId: Int?,
    @ColumnInfo(name = "name")
    var name: String?,
    @ColumnInfo(name = "path")
    var path: String?,
    @ColumnInfo(name = "length")
    var length: Int?,
    @ColumnInfo(name = "size")
    var size: Int?,
    @ColumnInfo(name = "date")
    var date: Long?)