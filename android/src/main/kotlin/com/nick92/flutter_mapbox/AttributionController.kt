package com.nick92.flutter_mapbox

import com.mapbox.maps.MapView
import com.nick92.flutter_mapbox.mapping.applyFromFLT
import com.nick92.flutter_mapbox.mapping.toFLT
import com.nick92.flutter_mapbox.pigeons.*
import com.mapbox.maps.plugin.attribution.attribution

class AttributionController(private val mapView: MapView) :
  AttributionSettingsInterface {
  override fun getSettings(): AttributionSettings = mapView.attribution.toFLT(mapView.context)
  override fun updateSettings(settings: AttributionSettings) {
    mapView.attribution.applyFromFLT(settings, mapView.context)
  }
}