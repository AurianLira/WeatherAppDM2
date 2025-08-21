package com.example.weatherappdm2.db.fb

import com.example.weatherappdm2.model.City
import com.google.android.gms.maps.model.LatLng

class FBCity {
    var name: String? = null
    var lat: Double? = null
    var lng: Double? = null
    var monitored: Boolean = false

    fun toCity(): City {
        val latlng = if (lat != null && lng != null) LatLng(lat!!, lng!!) else null
        return City(
            name = name!!,
            weather = null,
            location = latlng,
            forecast = null,
            isMonitored = monitored
        )
    }
}

fun City.toFBCity(): FBCity {
    val fbCity = FBCity()
    fbCity.name = this.name
    fbCity.lat = this.location?.latitude ?: 0.0
    fbCity.lng = this.location?.longitude ?: 0.0
    fbCity.monitored = this.isMonitored
    return fbCity
}