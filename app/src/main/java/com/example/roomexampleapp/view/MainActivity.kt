package com.example.roomexampleapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomexampleapp.R
import com.example.roomexampleapp.SubscriberAdapter
import com.example.roomexampleapp.SubscriberViewModel
import com.example.roomexampleapp.SubscriberViewModelFactory
import com.example.roomexampleapp.databinding.ActivityMainBinding
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
        binding.subscriberRecyclerview.layoutManager = LinearLayoutManager(this)
        val subscriberAdapter = SubscriberAdapter { subscriber ->
            subscriberViewModel.selectedItem(subscriber)
        }
        binding.subscriberRecyclerview.adapter = subscriberAdapter
        subscriberViewModel.subscribers.observe(this) {
            subscriberAdapter.setSubscriberList(it)
            subscriberAdapter.notifyDataSetChanged()
        }
        subscriberViewModel.message.observe(this) {
            Toast.makeText(this, it.getContentIfNotHandled(), Toast.LENGTH_LONG).show()
        }
    }
}