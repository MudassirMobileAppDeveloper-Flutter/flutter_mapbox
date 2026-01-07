package com.nick92.flutter_mapbox

import com.mapbox.maps.MapView
import com.nick92.flutter_mapbox.mapping.applyFromFLT
import com.nick92.flutter_mapbox.mapping.toFLT
import com.nick92.flutter_mapbox.pigeons.*
import com.mapbox.maps.plugin.logo.logo

class LogoController(private val mapView: MapView) :
  LogoSettingsInterface {
  override fun getSettings(): LogoSettings = mapView.logo.toFLT(mapView.context)
  override fun updateSettings(settings: LogoSettings) {
    mapView.logo.applyFromFLT(settings, mapView.context)
  }
}