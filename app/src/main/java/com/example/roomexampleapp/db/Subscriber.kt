package com.example.roomexampleapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("subscriber_data_table")
data class Subscriber(
    @PrimaryKey(true)
    @ColumnInfo("subscriber_id")
    var id: Int,
    @ColumnInfo("subscriber_name")
    var name: String,
    @ColumnInfo("subscriber_email")
    var email: String
)