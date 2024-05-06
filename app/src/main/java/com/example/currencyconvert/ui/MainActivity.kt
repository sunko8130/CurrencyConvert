package com.example.currencyconvert.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.currencyconvert.data.local.currencies.Currency
import com.example.currencyconvert.data.local.exchangerates.ExchangeRate
import com.example.currencyconvert.databinding.ActivityMainBinding
import com.example.currencyconvert.ui.adapter.CurrencySpinnerAdapter
import com.example.currencyconvert.ui.adapter.ExchangeRatesAdapter
import com.example.currencyconvert.utils.Constants
import com.example.currencyconvert.utils.FetchUpdateData
import com.example.currencyconvert.utils.LoadingDialog
import com.example.currencyconvert.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var exchangeRatesAdapter: ExchangeRatesAdapter

    private val currencyConversionVM: CurrencyConversionViewModel by viewModels()

    private var currencies: List<Currency> = ArrayList()
    private var exchangeRates: List<ExchangeRate> = ArrayList()
    private var selectedCurrency: String? = null
    private var inputAmt: Double = 1.0

    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var fetchUpdateData: FetchUpdateData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog(this)

        setupRecyclerView()
        setupSpinnerView()
        setObservers()
        setListeners()

        // Fetch update data every 30 minutes
        fetchUpdateData = FetchUpdateData(currencyConversionVM)
        fetchUpdateData.startFetchingData()
    }

    private fun setupSpinnerView() {
        // Set a listener to handle currency selection
        binding.spinnerCurrencies.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCurrency = currencies[position].currencyCode
                    showLoadingDialog()
                    lifecycleScope.launch {
                        delay(2000)
                        hideLoadingDialog()
                        // Update exchange rates upon selected currency
                        if (binding.etAmount.text.isNullOrEmpty()) {
                            inputAmt = binding.etAmount.text.toString().toDoubleOrNull() ?: 1.0
                        }
                        val convertRates =
                            calculateExchangeRates(selectedCurrency!!, exchangeRates, inputAmt)
                        exchangeRatesAdapter.addExchangeRates(convertRates)
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun setupRecyclerView() {
        binding.rvExchangeRates.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 3)
            adapter = exchangeRatesAdapter
        }
    }

    private fun setObservers() {
        currencyConversionVM.currencies.observe(this) {
            it.getContentIfNotHandled().let { result ->
                when (result?.status) {
                    Status.SUCCESS -> {
                        result.data?.let { list -> currencies = list }
                        val currencySpinnerAdapter =
                            CurrencySpinnerAdapter(this@MainActivity, currencies)
                        binding.spinnerCurrencies.adapter = currencySpinnerAdapter

                        // Default USD
                        val defaultValue = "USD"
                        val defaultPosition =
                            currencies.indexOfFirst { currency -> currency.currencyCode == defaultValue }

                        if (defaultPosition != -1) {
                            binding.spinnerCurrencies.setSelection(defaultPosition)
                        }
                    }

                    Status.LOADING -> {
                        showLoadingDialog()
                    }

                    else -> {
                        Toast.makeText(
                            this@MainActivity,
                            result?.message ?: Constants.SOMETHING_WENT_WRONG,
                            Toast.LENGTH_SHORT
                        ).show()
                        hideLoadingDialog()
                    }
                }
            }
        }
        currencyConversionVM.exchangeRates.observe(this) {
            it.getContentIfNotHandled().let { result ->
                when (result?.status) {
                    Status.SUCCESS -> {
                        result.data?.let { list -> exchangeRates = list }
                        lifecycleScope.launch {
                            delay(2000)
                            hideLoadingDialog()
                        }
                    }

                    Status.LOADING -> {}
                    else -> {
                        Toast.makeText(
                            this@MainActivity,
                            result?.message ?: Constants.SOMETHING_WENT_WRONG,
                            Toast.LENGTH_SHORT
                        ).show()
                        hideLoadingDialog()
                    }
                }
            }
        }
    }

    private fun setListeners() {
        // Initial getting the currencies and exchanges rates
        currencyConversionVM.getCurrenciesAndExchangeRates(false)

        // Currency amount input listener
        var job: Job? = null
        binding.etAmount.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(300L)
                editable?.let {
                    // Update exchange rates upon input amount
                    inputAmt = editable.toString().toDoubleOrNull() ?: 1.0
                    val convertRates =
                        calculateExchangeRates(selectedCurrency!!, exchangeRates, inputAmt)
                    exchangeRatesAdapter.addExchangeRates(convertRates)
                }
            }
        }
    }

    private fun showLoadingDialog() {
        loadingDialog.show()
    }

    private fun hideLoadingDialog() {
        loadingDialog.dismiss()
    }

    private fun calculateExchangeRates(
        selectedCurrency: String,
        rates: List<ExchangeRate>,
        inputAmt: Double
    ): List<ExchangeRate> {
        val convertedRates = mutableListOf<ExchangeRate>()

        val selectedCurrencyRate = rates.find { it.currencyCode == selectedCurrency }
        val baseRate = selectedCurrencyRate?.amount ?: 1.0

        for (rate in rates) {
            val convertedAmount = (rate.amount / baseRate) * inputAmt
            val convertedRate =
                ExchangeRate(currencyCode = rate.currencyCode, amount = convertedAmount)
            convertedRates.add(convertedRate)
        }

        return convertedRates
    }

    override fun onDestroy() {
        super.onDestroy()
        // Stop update data calling when activity destroyed
        fetchUpdateData.stopFetchingData()
    }
}