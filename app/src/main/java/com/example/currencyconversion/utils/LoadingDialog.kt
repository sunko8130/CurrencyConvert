package com.example.currencyconversion.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.example.currencyconversion.databinding.LoadingLayoutBinding

class LoadingDialog(context: Context): Dialog(context) {
    private lateinit var binding: LoadingLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoadingLayoutBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        setCancelable(false)
    }
}