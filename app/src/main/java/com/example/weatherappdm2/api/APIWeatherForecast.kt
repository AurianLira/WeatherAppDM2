package com.example.weatherappdm2.api

data class APIWeatherForecast (
    var location: APILocation? = null,
    var current: APIWeatherForecast? = null,
    var forecast: APIForecast? = null
)