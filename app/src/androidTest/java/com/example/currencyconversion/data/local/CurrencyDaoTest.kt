package com.example.currencyconversion.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.currencyconversion.data.local.currencies.Currency
import com.example.currencyconversion.data.local.currencies.CurrencyDao
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
class CurrencyDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("db_test")
    lateinit var database: CurrencyConversionDb
    private lateinit var dao: CurrencyDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.currencyDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndDeleteCurrencies() = runBlockingTest {
        val currencies = listOf(
            Currency(1, "USD", "US Dollar"),
            Currency(2, "EUR", "Euro"),
            Currency(3, "JPY", "Japanese Yen")
        )

        dao.insertCurrencies(currencies)

        val insertedCurrencies = dao.getCurrencies()
        assertThat(insertedCurrencies).containsExactlyElementsIn(currencies)

        dao.deleteCurrencies()

        val remainingCurrencies = dao.getCurrencies()
        assertThat(remainingCurrencies).isEmpty()
    }
}