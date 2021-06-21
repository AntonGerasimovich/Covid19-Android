package com.example.covid19.ui.components

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

@Composable
fun SpreadOfVirusMap(modifier: Modifier = Modifier, countryName: String) {
    val map = rememberMapViewWithLifecycle()
    val zoomLevel = remember { 5f }
    Card(
        modifier = modifier
            .padding(16.dp)
            .height(300.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10),
        elevation = 0.5.dp
    ) {
        MapContent(
            modifier = modifier.fillMaxSize(),
            countryName = countryName,
            zoomLevel = zoomLevel,
            mapView = map
        )
    }
}

@Composable
fun MapContent(
    modifier: Modifier = Modifier,
    countryName: String,
    zoomLevel: Float,
    mapView: MapView
) {
    val context = LocalContext.current
    val geocoder by remember { mutableStateOf(Geocoder(context, Locale.ENGLISH)) }
    AndroidView(modifier = modifier, factory = { mapView }) { map ->
        map.getMapAsync {
            val locations: List<Address> = geocoder.getFromLocationName(countryName, 1)
            if (locations.isNotEmpty()) {
                val latLng = LatLng(locations.first().latitude, locations.first().longitude)
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
                it.addMarker(MarkerOptions().position(latLng))
            }
        }
        map.isNestedScrollingEnabled = false
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context)
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
private fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }

@Preview
@Composable
fun PreviewMap() {
    //SpreadOfVirusMap(countryName = LatLng(53.0, 27.0))
}