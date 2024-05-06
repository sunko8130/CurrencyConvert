package com.example.currencyconvert.data.local.currencies

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class Currency(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val currencyCode: String = "",
    val currencyName: String = ""
)