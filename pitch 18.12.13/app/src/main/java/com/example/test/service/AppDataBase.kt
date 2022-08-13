package com.example.test.service
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.test.dao.AudioDao
import com.example.test.dao.UserDao
import com.example.test.dao.VideoDao
import com.example.test.entity.Audio
import com.example.test.entity.User
import com.example.test.entity.Video
import com.feng.kotlin.AppContext

@Database(entities = [User::class, Video::class, Audio::class],version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getUserDao(): UserDao
    abstract fun getVideoDao(): VideoDao
    abstract fun getAudioDao(): AudioDao
    companion object {
        @Volatile
        private var instance: AppDataBase? = null

        fun getDBInstace(): AppDataBase {
            if (instance == null) {

                synchronized(AppDataBase::class) {

                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            AppContext.instance(),
                            AppDataBase::class.java,
                            "pitch.db"
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return instance!!
        }

    }

}