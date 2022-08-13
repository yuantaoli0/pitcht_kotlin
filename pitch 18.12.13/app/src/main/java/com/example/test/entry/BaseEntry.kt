package com.example.test.entry


data class BaseEntry<T>(
    var code: Int,
    val data: T,
    var msg: String = ""
)