package com.example.currencyconversion.di

import android.content.Context
import androidx.room.Room
import com.example.currencyconversion.data.local.CurrencyConversionDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("db_test")
    fun provideInMemoryDb(@ApplicationContext context: Context)=
        Room.inMemoryDatabaseBuilder(context, CurrencyConversionDb::class.java)
            .allowMainThreadQueries()
            .build()

}