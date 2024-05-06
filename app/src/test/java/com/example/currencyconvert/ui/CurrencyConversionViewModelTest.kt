package com.example.currencyconvert.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.currencyconvert.MainCoroutineRule
import com.example.currencyconvert.data.local.currencies.Currency
import com.example.currencyconvert.data.local.exchangerates.ExchangeRate
import com.example.currencyconvert.getOrAwaitValueTest
import com.example.currencyconvert.repositories.FakeCurrencyConversionRepository
import com.example.currencyconvert.utils.Resource
import com.example.currencyconvert.utils.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CurrencyConversionViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: CurrencyConversionViewModel

    @Before
    fun setup() {
        viewModel = CurrencyConversionViewModel(FakeCurrencyConversionRepository())
    }

    /**
     * Currencies test cases
     */

    @Test
    fun `insert currencies check loading state, returns success`() {
        viewModel.updateCurrencies(Resource(Status.LOADING, ArrayList(), ""))
        val value = viewModel.currencies.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.LOADING)
    }

    @Test
    fun `insert currencies with invalid response, returns error`() {
        viewModel.updateCurrencies(Resource(Status.ERROR, null, ""))
        val value = viewModel.currencies.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert currencies with valid response, returns success`() {
        val currencies: ArrayList<Currency> = ArrayList()
        currencies.add(Currency(1, "USD"))
        currencies.add(Currency(2, "JPY"))
        val response = Resource(Status.SUCCESS, currencies, "")
        viewModel.updateCurrencies(response)
        val value = viewModel.currencies.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `insert currencies with valid response and verify data, returns success`() {
        val exchangeRates: ArrayList<Currency> = ArrayList()
        exchangeRates.add(Currency(1, "USD"))
        val response = Resource(Status.SUCCESS, exchangeRates, "")
        viewModel.updateCurrencies(response)
        val value = viewModel.currencies.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.data?.first()?.currencyCode).isEqualTo("USD")
    }

    /**
     * Currencies test cases
     */

    @Test
    fun `insert exchange rates check loading state, returns success`() {
        viewModel.updateExchangeRates(Resource(Status.LOADING, ArrayList(), ""))
        val value = viewModel.exchangeRates.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.LOADING)
    }

    @Test
    fun `insert exchange rates with invalid response, returns error`() {
        viewModel.updateExchangeRates(Resource(Status.ERROR, null, ""))
        val value = viewModel.exchangeRates.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert exchange rates with valid response, returns success`() {
        val exchangeRates: ArrayList<ExchangeRate> = ArrayList()
        exchangeRates.add(ExchangeRate(1, "USD", 3.673199))
        exchangeRates.add(ExchangeRate(2, "JPY", 144.905))

        val response = Resource(Status.SUCCESS, exchangeRates, "")
        viewModel.updateExchangeRates(response)
        val value = viewModel.exchangeRates.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `insert exchange rates with valid response and verify data, returns success`() {
        val exchangeRates: ArrayList<ExchangeRate> = ArrayList()
        exchangeRates.add(ExchangeRate(1, "USD", 3.673199))
        val response = Resource(Status.SUCCESS, exchangeRates, "")
        viewModel.updateExchangeRates(response)

        val value = viewModel.exchangeRates.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.data?.first()?.amount).isEqualTo(3.673199)
    }

}