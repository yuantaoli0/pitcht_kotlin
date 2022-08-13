package com.example.test.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.test.entity.User

@Dao
interface UserDao:BaseDao<User> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element:User)

    @Query("select * from User")
    fun getAllUsers():MutableList<User>

    @Query("select * from User where userId = :userId")
    fun getUser(userId:Int):User

    @Query("select * from User where name = :name")
    fun getUserByName(name:String):MutableList<User>

    @Query("select * from User where email = :email and password=:password")
    fun getUserByEmailAndPassword(email:String,password:String):MutableList<User>

    @Query("delete from User")
    fun deleteAll()

}