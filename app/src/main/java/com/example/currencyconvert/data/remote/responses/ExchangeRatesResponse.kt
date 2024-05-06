package com.example.currencyconvert.data.remote.responses

import com.google.gson.annotations.SerializedName

data class ExchangeRatesResponse(
	@SerializedName("base")
	val base: String,
	@SerializedName("rates")
	val rates: HashMap<String, Double> = HashMap()
)
