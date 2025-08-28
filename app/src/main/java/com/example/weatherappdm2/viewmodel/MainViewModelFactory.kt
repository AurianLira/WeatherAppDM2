package com.example.weatherappdm2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherappdm2.api.WeatherService
import com.example.weatherappdm2.monitor.ForecastMonitor
import com.example.weatherappdm2.repo.Repository

class MainViewModelFactory(
    private val repository: Repository,
    private val service: WeatherService,
    private val monitor: ForecastMonitor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository, service, monitor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
