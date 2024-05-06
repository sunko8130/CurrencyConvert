package com.example.currencyconvert.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.currencyconvert.data.local.exchangerates.ExchangeRate
import com.example.currencyconvert.data.local.exchangerates.ExchangeRateDao
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ExchangeRateDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("db_test")
    lateinit var database: CurrencyConversionDb
    private lateinit var dao: ExchangeRateDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.exchangeRatesDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveExchangeRates() = runBlockingTest {
        val exchangeRates = listOf(
            ExchangeRate(1,"USD", 1.0),
            ExchangeRate(2,"EUR", 0.85),
        )

        dao.insertExchangeRates(exchangeRates)

        val retrievedRates = dao.getExchangeRates()
        assertThat(retrievedRates).isEqualTo(exchangeRates)
    }

    @Test
    fun updateExchangeRates() = runBlockingTest {
        val initialExchangeRates = listOf(
            ExchangeRate(1,"USD", 1.0),
            ExchangeRate(2,"EUR", 0.85),
        )
        dao.insertExchangeRates(initialExchangeRates)

        val updatedExchangeRates = initialExchangeRates.map {
            it.copy(amount = it.amount * 1.0)
        }
        dao.updateExchangeRates(updatedExchangeRates)

        val retrievedRates = dao.getExchangeRates()
        assertThat(retrievedRates).isEqualTo(updatedExchangeRates)
    }

    @Test
    fun deleteExchangeRates() = runBlockingTest {
        val exchangeRates = listOf(
            ExchangeRate(1,"USD", 1.0),
            ExchangeRate(2,"EUR", 0.85),
        )
        dao.insertExchangeRates(exchangeRates)

        dao.deleteExchangeRates()

        val retrievedRates = dao.getExchangeRates()
        assertThat(retrievedRates).isEmpty()
    }


}