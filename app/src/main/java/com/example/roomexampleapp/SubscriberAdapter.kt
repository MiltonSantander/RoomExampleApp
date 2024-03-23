package com.example.roomexampleapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.roomexampleapp.databinding.ListItemBinding
import com.example.roomexampleapp.db.Subscriber

class SubscriberAdapter(
    private val subscriberList: List<Subscriber>,
    private val callback: (Subscriber) -> Unit
) :
    RecyclerView.Adapter<SubscriberAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subscriber: Subscriber) {
            binding.subscriberName.text = subscriber.name
            binding.subscriberEmail.text = subscriber.email
            binding.cardView.setOnClickListener { callback(subscriber) }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubscriberAdapter.ViewHolder {
        val binding: ListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriberAdapter.ViewHolder, position: Int) {
        holder.bind(subscriberList[position])
    }

    override fun getItemCount(): Int {
        return subscriberList.size
    }
}