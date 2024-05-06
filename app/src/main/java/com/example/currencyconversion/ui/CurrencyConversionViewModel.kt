package com.example.currencyconversion.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconversion.data.local.currencies.Currency
import com.example.currencyconversion.data.local.exchangerates.ExchangeRate
import com.example.currencyconversion.data.remote.responses.ExchangeRatesResponse
import com.example.currencyconversion.repositories.CurrencyConversionRepository
import com.example.currencyconversion.utils.Constants
import com.example.currencyconversion.utils.Event
import com.example.currencyconversion.utils.Resource
import com.example.currencyconversion.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyConversionViewModel @Inject constructor(
    private val repository: CurrencyConversionRepository
) : ViewModel() {

    private val _currencies = MutableLiveData<Event<Resource<ArrayList<Currency>>>>()
    private val _exchangeRates = MutableLiveData<Event<Resource<ArrayList<ExchangeRate>>>>()

    val currencies: LiveData<Event<Resource<ArrayList<Currency>>>> = _currencies
    val exchangeRates: LiveData<Event<Resource<ArrayList<ExchangeRate>>>> = _exchangeRates

    fun updateCurrencies(response: Resource<ArrayList<Currency>>) {
        _currencies.postValue(Event(response))
    }

    fun updateExchangeRates(response: Resource<ArrayList<ExchangeRate>>) {
        _exchangeRates.postValue(Event(response))
    }

    fun deleteCurrencies() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCurrencies()
    }

    fun deleteExchangeRates() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteExchangeRates()
    }

    private fun insertCurrenciesIntoDb(currencies: List<Currency>) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCurrencies(currencies)
        }

    private fun insertExchangeRatesIntoDb(exchangeRates: List<ExchangeRate>) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertExchangeRates(exchangeRates)
        }

    /**
     * Get currencies and exchange rates from DB and Server
     */
    fun getCurrenciesAndExchangeRates(repeatedCall: Boolean) {
        _currencies.value = Event(Resource.loading(null))
        _exchangeRates.value = Event(Resource.loading(null))

        viewModelScope.launch(Dispatchers.IO) {
            val currenciesDbResponse = ArrayList(repository.getAllCurrencies())
            val exchangeRatesDbResponse = ArrayList(repository.getAllExchangeRates())
            viewModelScope.launch {
                if (!repeatedCall) {
                    updateCurrencies(Resource.success(currenciesDbResponse))
                    updateExchangeRates(Resource.success(exchangeRatesDbResponse))
                    if (currenciesDbResponse.isEmpty()) {
                        // Get currencies from server
                        val currenciesApiResponse = repository.getCurrencies()
                        currenciesResponse(currenciesApiResponse)
                    }
                } else {
                    // Get currencies from server
                    val currenciesApiResponse = repository.getCurrencies()
                    currenciesResponse(currenciesApiResponse)
                }
            }
        }
    }

    private fun currenciesResponse(currenciesApiResponse: Resource<Map<String, String>>) {
        val currencies: ArrayList<Currency> = ArrayList()
        when (currenciesApiResponse.status) {
            Status.SUCCESS -> {
                viewModelScope.launch {
                    // Get exchange rates from server
                    val exchangeRatesApiResponse = repository.getExchangeRates()
                    exchangeRatesResponse(exchangeRatesApiResponse)
                }

                // Add currencies into DB
                currenciesApiResponse.data?.forEach { (key, value) ->
                    currencies.add(
                        Currency(
                            currencyCode = key,
                            currencyName = value
                        )
                    )
                }
                if (currencies.isNotEmpty()) {
                    deleteCurrencies()
                    insertCurrenciesIntoDb(currencies)
                }
                updateCurrencies(Resource.success(currencies))
            }

            else -> {
                updateCurrencies(
                    Resource.error(
                        currenciesApiResponse.message ?: Constants.NO_INTERNET,
                        currencies
                    )
                )
            }
        }
    }

    private fun exchangeRatesResponse(
        exchangeRatesApiResponse: Resource<ExchangeRatesResponse>
    ) {
        val exchangeRates: ArrayList<ExchangeRate> = ArrayList()
        when (exchangeRatesApiResponse.status) {
            Status.SUCCESS -> {
                // Add exchange rates into DB
                exchangeRatesApiResponse.data?.rates?.forEach { (currencyCode, amount) ->
                    exchangeRates.add(
                        ExchangeRate(
                            currencyCode = currencyCode,
                            amount = amount
                        )
                    )
                }
                if (exchangeRates.isNotEmpty()) {
                    deleteExchangeRates()
                    insertExchangeRatesIntoDb(exchangeRates)
                }
                updateExchangeRates(Resource.success(exchangeRates))
            }

            else -> {
                updateExchangeRates(
                    Resource.error(
                        exchangeRatesApiResponse.message ?: Constants.NO_INTERNET, exchangeRates
                    )
                )
            }
        }
    }


}