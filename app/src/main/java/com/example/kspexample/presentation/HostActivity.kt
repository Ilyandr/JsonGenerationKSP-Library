package com.example.kspexample.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kspexample.databinding.ActivityHostBinding
import com.example.kspexample.repository.data.generated.UserEntityJson

class HostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UserEntityJson
    }
}