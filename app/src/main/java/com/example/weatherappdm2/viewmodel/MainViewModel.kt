package com.example.weatherappdm2.viewmodel

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.weatherappdm2.Route
import com.example.weatherappdm2.api.WeatherService
import com.example.weatherappdm2.api.toForecast
import com.example.weatherappdm2.api.toWeather
import com.example.weatherappdm2.model.City
import com.example.weatherappdm2.model.User
import com.example.weatherappdm2.monitor.ForecastMonitor
import com.example.weatherappdm2.repo.Repository
import com.google.android.gms.maps.model.LatLng

class MainViewModel(
    private val repository: Repository,
    private val service: WeatherService,
    private val monitor: ForecastMonitor
) : ViewModel(), Repository.Listener {

    private val _cities = mutableStateMapOf<String, City>()
    val cities: List<City>
        get() = _cities.values.toList()

    private val _user = mutableStateOf<User?>(null)
    val user: User?
        get() = _user.value

    private var _city = mutableStateOf<City?>(null)
    var city: City?
        get() = _city.value
        set(tmp) { _city.value = tmp?.copy() }

    private var _page = mutableStateOf<Route>(Route.Home)
    var page: Route
        get() = _page.value
        set(tmp) { _page.value = tmp }

    init {
        repository.setListener(this)
    }

    fun remove(city: City) {
        repository.remove(city)
        monitor.cancelCity(city)
    }

    fun update(city: City) {
        repository.update(city)
        monitor.updateCity(city)
    }

    override fun onUserLoaded(user: User) {
        _user.value = user
    }

    override fun onUserSignOut() {
        _user.value = null
        _cities.clear()
        _city.value = null
        monitor.cancelAll()
    }

    override fun onCityAdded(city: City) {
        _cities[city.name] = city
        monitor.updateCity(city)
    }

    override fun onCityUpdated(city: City) {
        val oldCity = _cities[city.name]
        _cities.remove(city.name)
        _cities[city.name] = city.copy(
            weather = oldCity?.weather,
            forecast = oldCity?.forecast
        )
        if (_city.value?.name == city.name) {
            _city.value = _cities[city.name]
        }
        monitor.updateCity(_cities[city.name]!!)
    }

    override fun onCityRemoved(city: City) {
        _cities.remove(city.name)
        monitor.cancelCity(city)
        if (_city.value?.name == city.name) {
            _city.value = null
        }
    }

    fun add(name: String) {
        service.getLocation(name) { lat, lng ->
            if (lat != null && lng != null) {
                repository.add(City(name = name, location = LatLng(lat, lng)))
            }
        }
    }

    fun add(location: LatLng) {
        service.getName(location.latitude, location.longitude) { name ->
            if (name != null) {
                repository.add(City(name = name, location = location))
            }
        }
    }

    fun loadWeather(name: String) {
        service.getWeather(name) { apiWeather ->
            val newCity = _cities[name]!!.copy(weather = apiWeather?.toWeather())
            _cities.remove(name)
            _cities[name] = newCity
        }
    }

    fun loadForecast(name: String) {
        service.getForecast(name) { apiForecast ->
            val newCity = _cities[name]!!.copy(forecast = apiForecast?.toForecast())
            _cities.remove(name)
            _cities[name] = newCity
            city = if (city?.name == name) newCity else city
        }
    }

    fun loadBitmap(name: String) {
        val city = _cities[name]
        service.getBitmap(city?.weather!!.imgUrl) { bitmap ->
            val newCity = city.copy(
                weather = city.weather?.copy(bitmap = bitmap)
            )
            _cities.remove(name)
            _cities[name] = newCity
        }
    }
}