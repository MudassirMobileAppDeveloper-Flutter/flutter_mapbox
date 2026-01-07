package com.nick92.flutter_mapbox

import android.content.Context
import com.mapbox.maps.MapView
import com.nick92.flutter_mapbox.mapping.applyFromFLT
import com.nick92.flutter_mapbox.mapping.toFLT
import com.nick92.flutter_mapbox.pigeons.*
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location

class LocationComponentController(
  private val mapView: MapView,
  private val context: Context
) : _LocationComponentSettingsInterface {
  override fun getSettings(): LocationComponentSettings = mapView.location.toFLT(context)

  override fun updateSettings(settings: LocationComponentSettings, useDefaultPuck2DIfNeeded: Boolean) {
    if (useDefaultPuck2DIfNeeded) {
      mapView.location.locationPuck = createDefault2DPuck(withBearing = settings.puckBearingEnabled == true)
    }
    mapView.location.applyFromFLT(settings, useDefaultPuck2DIfNeeded, context)
  }
}