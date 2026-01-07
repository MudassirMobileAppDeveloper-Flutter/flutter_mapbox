package com.nick92.flutter_mapbox

import com.mapbox.maps.MapView
import com.nick92.flutter_mapbox.mapping.applyFromFLT
import com.nick92.flutter_mapbox.mapping.toFLT
import com.nick92.flutter_mapbox.pigeons.*
import com.mapbox.maps.plugin.scalebar.scalebar

class ScaleBarController(private val mapView: MapView) :
  ScaleBarSettingsInterface {
  override fun getSettings(): ScaleBarSettings = mapView.scalebar.toFLT(mapView.context)
  override fun updateSettings(settings: ScaleBarSettings) {
    mapView.scalebar.applyFromFLT(settings, mapView.context)
  }
}