package com.example.test.dao

import androidx.room.*
import com.example.test.entity.Audio
import com.example.test.entity.Video

@Dao
interface AudioDao:BaseDao<Audio> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element:Audio)

    @Query("select * from Audio Order By date asc")
    fun getAllAudios():MutableList<Audio>

    @Query("select * from Audio where audioId = :audioId")
    fun getAudio(audioId:Int):Audio

    @Query("select * from Audio where path = :path")
    fun getAudioByPath(path:String):Audio
}