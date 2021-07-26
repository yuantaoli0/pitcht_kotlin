package com.example.test.service
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.test.dao.UserDao
import com.example.test.entity.User
import com.feng.kotlin.AppContext

@Database(entities = [User::class],version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

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