// This file is generated.
package com.nick92.flutter_mapbox.mapping

import android.content.Context
import com.nick92.flutter_mapbox.pigeons.*
import com.nick92.flutter_mapbox.toDevicePixels
import com.nick92.flutter_mapbox.toLogicalPixels
import com.mapbox.maps.plugin.logo.generated.LogoSettingsInterface

fun LogoSettingsInterface.applyFromFLT(settings: LogoSettings, context: Context) {
  updateSettings {
    settings.enabled?.let { this.enabled = it }
    settings.position?.let { this.position = it.toPosition() }
    settings.marginLeft?.let { this.marginLeft = it.toDevicePixels(context) }
    settings.marginTop?.let { this.marginTop = it.toDevicePixels(context) }
    settings.marginRight?.let { this.marginRight = it.toDevicePixels(context) }
    settings.marginBottom?.let { this.marginBottom = it.toDevicePixels(context) }
  }
}

fun LogoSettingsInterface.toFLT(context: Context) = LogoSettings(
  enabled = enabled,
  position = position.toOrnamentPosition(),
  marginLeft = marginLeft.toLogicalPixels(context),
  marginTop = marginTop.toLogicalPixels(context),
  marginRight = marginRight.toLogicalPixels(context),
  marginBottom = marginBottom.toLogicalPixels(context),
)

// End of generated file.