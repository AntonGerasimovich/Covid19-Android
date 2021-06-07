package com.example.covid19.ui.components

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.covid19.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun SpreadOfVirusMap(modifier: Modifier = Modifier, latLng: LatLng) {
    val map = rememberMapViewWithLifecycle()
    val zoomLevel = remember { 17f }
    Card(
        modifier = modifier
            .padding(16.dp)
            .height(300.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(5),
        elevation = 2.dp
    ) {
        MapContent(
            modifier = modifier.fillMaxSize(),
            latLng = latLng,
            zoomLevel = zoomLevel,
            mapView = map
        )
    }
}

@Composable
fun MapContent(
    modifier: Modifier = Modifier,
    latLng: LatLng,
    zoomLevel: Float,
    mapView: MapView
) {
    AndroidView(modifier = modifier, factory = { mapView }) { map ->
        map.getMapAsync {
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
            it.addMarker(MarkerOptions().position(latLng))
        }
        map.isNestedScrollingEnabled = false
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
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
    SpreadOfVirusMap(latLng = LatLng(53.0, 27.0))
}