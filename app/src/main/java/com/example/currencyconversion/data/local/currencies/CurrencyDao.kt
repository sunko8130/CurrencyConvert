package com.example.currencyconversion.data.local.currencies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencies(currencyItems: List<Currency>)

    @Query("SELECT * FROM currency")
    fun getCurrencies(): List<Currency>

    @Query("DELETE FROM currency")
    suspend fun deleteCurrencies()
}