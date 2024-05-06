package com.example.currencyconversion.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.currencyconversion.data.local.currencies.Currency
import com.example.currencyconversion.databinding.CurrencyLayoutBinding

class CurrencySpinnerAdapter(
context: Context,
currencies: List<Currency>
) : ArrayAdapter<Currency>(context,0, currencies) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        val binding: CurrencyLayoutBinding

        if (convertView == null) {
            binding = CurrencyLayoutBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
            viewHolder = ViewHolder(binding)
            binding.root.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            binding = viewHolder.binding
        }

        val currency = getItem(position)
        viewHolder.tvCurrencyCode.text = String.format("%s", currency?.currencyCode)

        // Hide the currency name and view divider
        viewHolder.tvCurrency.visibility = View.GONE
        viewHolder.viewDivider.visibility = View.GONE

        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        val binding: CurrencyLayoutBinding

        if (convertView == null) {
            binding = CurrencyLayoutBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
            viewHolder = ViewHolder(binding)
            binding.root.tag = viewHolder
        } else {
            viewHolder = convertView.tag as ViewHolder
            binding = viewHolder.binding
        }

        val currency = getItem(position)
        viewHolder.tvCurrencyCode.text = String.format("%s", currency?.currencyCode)
        viewHolder.tvCurrency.text = String.format("%s", currency?.currencyName)

        return binding.root
    }

    private class ViewHolder(val binding: CurrencyLayoutBinding) {
        val tvCurrencyCode: TextView = binding.tvCurrencyCode
        val tvCurrency: TextView = binding.tvCurrency
        val viewDivider: View = binding.viewDivider
    }
}

