package com.example.currencyconvert.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.currencyconvert.data.local.currencies.Currency
import com.example.currencyconvert.data.local.currencies.CurrencyDao
import com.example.currencyconvert.data.local.exchangerates.ExchangeRate
import com.example.currencyconvert.data.local.exchangerates.ExchangeRateDao

@Database(entities = [Currency::class, ExchangeRate::class], version = 1, exportSchema = false)
abstract class CurrencyConversionDb : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao
    abstract fun exchangeRatesDao(): ExchangeRateDao
}