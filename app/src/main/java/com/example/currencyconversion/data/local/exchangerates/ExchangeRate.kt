package com.example.currencyconversion.data.local.exchangerates

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRate(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val currencyCode: String = "",
    val amount: Double = 0.0
)
