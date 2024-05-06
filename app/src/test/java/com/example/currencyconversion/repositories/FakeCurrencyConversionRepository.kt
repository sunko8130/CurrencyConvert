package com.example.currencyconversion.repositories

import androidx.lifecycle.MutableLiveData
import com.example.currencyconversion.data.local.currencies.Currency
import com.example.currencyconversion.data.local.exchangerates.ExchangeRate
import com.example.currencyconversion.data.remote.responses.ExchangeRatesResponse
import com.example.currencyconversion.utils.Resource

class FakeCurrencyConversionRepository : CurrencyConversionRepository {

    private val currencies = mutableListOf<Currency>()
    private val observableCurrencies = MutableLiveData<List<Currency>>(currencies)

    private val exchangeRates = mutableListOf<ExchangeRate>()
    private val observableExchangeRates = MutableLiveData<List<ExchangeRate>>(exchangeRates)


    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun insertCurrencies(currencies: List<Currency>) {}

    override suspend fun deleteCurrencies() {}

    override suspend fun getAllCurrencies(): List<Currency> {
        return emptyList()
    }

    override suspend fun insertExchangeRates(exchangeRates: List<ExchangeRate>) {}

    override suspend fun deleteExchangeRates() {}

    override suspend fun getAllExchangeRates(): List<ExchangeRate> {
        return emptyList()
    }

    override suspend fun getCurrencies(): Resource<Map<String, String>> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)

        } else {
            val currencies = hashMapOf<String, String>()
            currencies["USD"] = "US Dollar"
            currencies["JPY"] = "Japanese Yen"
            val response = currencies
            Resource.success(response)
        }
    }

    override suspend fun getExchangeRates(): Resource<ExchangeRatesResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)

        } else {
            val rates = hashMapOf<String, Double>()
            rates["USD"] = 1.0
            rates["EUR"] = 0.85
            val response = ExchangeRatesResponse("USD", rates)
            Resource.success(response)
        }
    }
}