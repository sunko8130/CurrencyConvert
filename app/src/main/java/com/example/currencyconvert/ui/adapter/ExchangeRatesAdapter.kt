package com.example.currencyconvert.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconvert.data.local.exchangerates.ExchangeRate
import com.example.currencyconvert.databinding.ExchangeRatesLayoutBinding
import javax.inject.Inject

class ExchangeRatesAdapter @Inject constructor() :
    RecyclerView.Adapter<ExchangeRatesAdapter.ExchangeRatesVH>() {
    private var exchangeRates: ArrayList<ExchangeRate> = ArrayList()

    fun addExchangeRates(exchangeRates: List<ExchangeRate>){
        this.exchangeRates.clear()
        this.exchangeRates.addAll(exchangeRates)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRatesVH {
        val binding =
            ExchangeRatesLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExchangeRatesVH(binding)
    }

    override fun getItemCount(): Int = exchangeRates.size

    override fun onBindViewHolder(holder: ExchangeRatesVH, position: Int) {
        holder.bind(exchangeRates[position])
    }

    class ExchangeRatesVH(private val binding: ExchangeRatesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(exchangeRate: ExchangeRate) {
            binding.tvCurrencyCode.text = String.format("%s", exchangeRate.currencyCode)
            binding.tvExchangeRate.text = String.format("%s", exchangeRate.amount)
        }
    }
}