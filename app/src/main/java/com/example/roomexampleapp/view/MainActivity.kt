package com.example.roomexampleapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.roomexampleapp.R
import com.example.roomexampleapp.SubscriberViewModel
import com.example.roomexampleapp.SubscriberViewModelFactory
import com.example.roomexampleapp.databinding.ActivityMainBinding
import com.example.roomexampleapp.db.SubscriberDAO
import com.example.roomexampleapp.db.SubscriberDatabase
import com.example.roomexampleapp.db.SubscriberRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        subscriberViewModel = ViewModelProvider(
            this,
            SubscriberViewModelFactory(
                SubscriberRepository(
                    SubscriberDatabase.getInstance(application).subscriberDAO
                )
            )
        )[SubscriberViewModel::class.java]
        binding.subscriberViewModel = subscriberViewModel
        binding.lifecycleOwner = this
    }
}