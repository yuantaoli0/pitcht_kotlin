package com.example.test.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Audio")
data class Audio(
    @PrimaryKey(autoGenerate = true)
    var audioId: Int?,
    @ColumnInfo(name = "name")
    var name: String?,
    @ColumnInfo(name = "path")
    var path: String?,
    @ColumnInfo(name = "length")
    var length: Int?,
    @ColumnInfo(name = "date")
    var date: Long?)
