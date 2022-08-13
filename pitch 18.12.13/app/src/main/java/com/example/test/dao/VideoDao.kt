package com.example.test.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.test.entity.Video

@Dao
interface VideoDao:BaseDao<Video> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element:Video)

    @Query("select * from Video Order by date asc")
    fun getAllVideos():MutableList<Video>

    @Query("select * from Video where videoId = :videoId")
    fun getVideo(videoId:Int):Video

    @Query("select * from Video where path = :path")
    fun getVideoByPath(path:String):Video
}