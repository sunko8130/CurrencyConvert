package com.example.currencyconversion.repositories

import com.example.currencyconversion.data.local.currencies.Currency
import com.example.currencyconversion.data.local.currencies.CurrencyDao
import com.example.currencyconversion.data.local.exchangerates.ExchangeRate
import com.example.currencyconversion.data.local.exchangerates.ExchangeRateDao
import com.example.currencyconversion.data.remote.APIService
import com.example.currencyconversion.data.remote.responses.ExchangeRatesResponse
import com.example.currencyconversion.utils.Constants
import com.example.currencyconversion.utils.Resource
import javax.inject.Inject

class BaseCurrencyConversionRepository @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val exchangeRatesDao: ExchangeRateDao,
    private val apiService: APIService
) : CurrencyConversionRepository{
    override suspend fun insertCurrencies(currencies: List<Currency>) {
        currencyDao.insertCurrencies(currencies)
    }

    override suspend fun insertExchangeRates(exchangeRates: List<ExchangeRate>) {
        exchangeRatesDao.insertExchangeRates(exchangeRates)
    }

    override suspend fun getAllCurrencies(): List<Currency> {
        return currencyDao.getCurrencies()
    }

    override suspend fun getAllExchangeRates(): List<ExchangeRate> {
        return exchangeRatesDao.getExchangeRates()
    }

    override suspend fun deleteCurrencies() {
        currencyDao.deleteCurrencies()
    }

    override suspend fun deleteExchangeRates() {
        exchangeRatesDao.deleteExchangeRates()
    }

    /**
     * fetch exchange rates
     */
    override suspend fun getExchangeRates(): Resource<ExchangeRatesResponse> {
        return try {
            val response = apiService.getExchangeRates()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(Constants.SOMETHING_WENT_WRONG, null)
            } else {
                Resource.error(Constants.SOMETHING_WENT_WRONG, null)
            }
        } catch (e: Exception) {
            Resource.error(Constants.NO_INTERNET, null)
        }
    }

    /**
     * fetch currencies
     */
    override suspend fun getCurrencies(): Resource<Map<String, String>> {
        return try {
            val response = apiService.getCurrencies()
            if (response.isSuccessful) {
                val currenciesMap = response.body()
                currenciesMap?.let {
                    return Resource.success(it)
                } ?: Resource.error(Constants.SOMETHING_WENT_WRONG, null)
            } else {
                Resource.error(Constants.SOMETHING_WENT_WRONG, null)
            }
        } catch (e: Exception) {
            Resource.error(Constants.NO_INTERNET, null)
        }
    }
}