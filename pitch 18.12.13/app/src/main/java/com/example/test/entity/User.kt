package com.example.test.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    var userId: Int?,
    @ColumnInfo(name = "name")
    var name: String?,
    @ColumnInfo(name = "username")
    var username: String?,
    @ColumnInfo(name = "email")
    var email: String?,
    @ColumnInfo(name = "password")
    var password: String?,
    @ColumnInfo(name = "website")
    var website: String?,
    @ColumnInfo(name = "twoFactorAuthOn")
    var twoFactorAuthOn: Boolean?,
    @ColumnInfo(name = "phoneNumber")
    var phoneNumber: String?,
    @ColumnInfo(name = "phoneCountryCode")
    var phoneCountryCode: String?,
    @ColumnInfo(name = "desc")
    var desc: String?)