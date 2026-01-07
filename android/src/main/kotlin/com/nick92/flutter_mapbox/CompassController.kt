package com.nick92.flutter_mapbox

import com.mapbox.maps.MapView
import com.nick92.flutter_mapbox.mapping.applyFromFLT
import com.nick92.flutter_mapbox.mapping.toFLT
import com.nick92.flutter_mapbox.pigeons.*
import com.mapbox.maps.plugin.compass.compass

class CompassController(private val mapView: MapView) :
  CompassSettingsInterface {
  override fun getSettings(): CompassSettings = mapView.compass.toFLT(mapView.context)
  override fun updateSettings(settings: CompassSettings) {
    mapView.compass.applyFromFLT(settings, mapView.context)
  }
}