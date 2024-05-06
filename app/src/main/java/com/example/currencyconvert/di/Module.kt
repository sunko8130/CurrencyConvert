package com.example.currencyconvert.di

import android.content.Context
import androidx.room.Room
import com.example.currencyconvert.BuildConfig
import com.example.currencyconvert.data.local.CurrencyConversionDb
import com.example.currencyconvert.data.local.currencies.CurrencyDao
import com.example.currencyconvert.data.local.exchangerates.ExchangeRateDao
import com.example.currencyconvert.data.remote.APIService
import com.example.currencyconvert.repositories.CurrencyConversionRepository
import com.example.currencyconvert.repositories.BaseCurrencyConversionRepository
import com.example.currencyconvert.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideApiService(): APIService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(APIService::class.java)
    }

    @Singleton
    @Provides
    fun provideCurrencyConversionDb(@ApplicationContext context: Context): CurrencyConversionDb =
        Room.databaseBuilder(
            context, CurrencyConversionDb::class.java,
            Constants.DATABASE_NAME
        ).allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun provideCurrencyDao(currencyConversionDb: CurrencyConversionDb) =
        currencyConversionDb.currencyDao()

    @Singleton
    @Provides
    fun provideExchangeDao(currencyConversionDb: CurrencyConversionDb) =
        currencyConversionDb.exchangeRatesDao()

    @Singleton
    @Provides
    fun provideCurrencyConversionRepository(
        currencyDao: CurrencyDao,
        exchangeRateDao: ExchangeRateDao,
        apiService: APIService
    ) =
        BaseCurrencyConversionRepository(
            currencyDao,
            exchangeRateDao,
            apiService
        ) as CurrencyConversionRepository
}