package com.example.weatherappdm2

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.weatherappdm2.viewmodel.MainViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    val camPosState = rememberCameraPositionState()
    val context = LocalContext.current

    val hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        onMapClick = {
            viewModel.add(it)
        },
        cameraPositionState = camPosState,
        properties = MapProperties(isMyLocationEnabled = hasLocationPermission),
        uiSettings = MapUiSettings(myLocationButtonEnabled = true)
    ) {
        viewModel.cities.forEach {
            if (it.location != null) {
                LaunchedEffect(it.name) {
                    if (it.weather == null) {
                        viewModel.loadWeather(it.name)
                    }
                }
                LaunchedEffect(it.weather) {
                    if (it.weather != null && it.weather!!.bitmap == null) {
                        viewModel.loadBitmap(it.name)
                    }
                }
                val image = it.weather?.bitmap ?: ContextCompat.getDrawable(context, R.drawable.loading)!!
                    .toBitmap()
                val marker = BitmapDescriptorFactory
                    .fromBitmap(image.scale(120, 120))
                Marker(
                    state = MarkerState(position = it.location),
                    title = it.name,
                    icon = marker,
                    snippet = it.weather?.desc ?: "Carregando..."
                )
            }
        }
    }
}

fun Bitmap.scale(width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(this, width, height, false)
}