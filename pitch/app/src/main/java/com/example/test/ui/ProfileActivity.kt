package com.example.test.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.test.databinding.ActivityProfileBinding
import com.feng.kotlin.AppContext

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
    }

    fun initData(){
        binding.tvName.text = AppContext.user?.userName ?: "";
        binding.tvUserName.text = AppContext.user?.userName ?:"" ;
    }
}