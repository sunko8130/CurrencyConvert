package com.example.currencyconversion.data.remote

import com.example.currencyconversion.BuildConfig
import com.example.currencyconversion.data.remote.responses.ExchangeRatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET(APIEndPoints.CURRENCIES)
    suspend fun getCurrencies(): Response<Map<String, String>>

    @GET(APIEndPoints.EXCHANGE_RATES)
    suspend fun getExchangeRates(
        @Query("app_id") apiKey: String = BuildConfig.API_KEY
    ) : Response<ExchangeRatesResponse>

}