package com.example.weatherappdm2.model

import com.google.android.gms.maps.model.LatLng

data class City (
    val name : String,
    val weather: Weather? = null,
    val location: LatLng? = null
)