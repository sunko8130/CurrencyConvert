package com.example.currencyconversion.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.currencyconversion.ui.CurrencyConversionViewModel
import java.util.Timer
import java.util.TimerTask

class FetchUpdateData(private val currencyConversionVM: CurrencyConversionViewModel) {
    private var timer: Timer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var fetchCountdown = 30 // Initial countdown in minutes
    private var secondsPassed = 0

    fun startFetchingData() {
        // Schedule a task to run every second
        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                if (fetchCountdown > 0) {
                    logCountdown(fetchCountdown, secondsPassed)
                    secondsPassed++
                    if (secondsPassed == 60) {
                        fetchCountdown--
                        secondsPassed = 0
                    }
                } else {
                    fetchCountdown = 30
                    fetchUpdate()
                }
            }
        }, 0, 1000)
    }

    private fun fetchUpdate() {
        handler.post {
            currencyConversionVM.getCurrenciesAndExchangeRates(true)
        }
    }

    fun stopFetchingData() {
        timer?.cancel()
        timer = null
    }

    private fun logCountdown(minutesRemaining: Int, secondsPassed: Int) {
        Log.d("FetchUpdateData","Fetching data in $minutesRemaining minutes and $secondsPassed seconds")
    }

}

