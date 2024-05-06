package com.example.currencyconversion.repositories

import com.example.currencyconversion.data.local.currencies.Currency
import com.example.currencyconversion.data.local.exchangerates.ExchangeRate
import com.example.currencyconversion.data.remote.responses.ExchangeRatesResponse
import com.example.currencyconversion.utils.Resource

interface CurrencyConversionRepository {

    /**
     * Currencies
     */
    suspend fun insertCurrencies(currencies: List<Currency>)
    suspend fun deleteCurrencies()
    suspend fun getAllCurrencies(): List<Currency>
    suspend fun getCurrencies(): Resource<Map<String, String>>

    /**
     * Exchange Rates
     */
    suspend fun insertExchangeRates(exchangeRates: List<ExchangeRate>)
    suspend fun deleteExchangeRates()
    suspend fun getAllExchangeRates(): List<ExchangeRate>
    suspend fun getExchangeRates(): Resource<ExchangeRatesResponse>
}