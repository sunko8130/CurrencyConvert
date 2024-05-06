package com.example.currencyconversion.data.local.exchangerates

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExchangeRateDao {
    @Update
    suspend fun updateExchangeRates(exchangeRates: List<ExchangeRate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExchangeRates(exchangeItems: List<ExchangeRate>)

    @Query("SELECT * FROM exchange_rates")
    fun getExchangeRates(): List<ExchangeRate>

    @Query("DELETE FROM exchange_rates")
    suspend fun deleteExchangeRates()
}