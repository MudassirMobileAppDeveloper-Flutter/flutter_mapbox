package com.nick92.flutter_mapbox.offline

import com.nick92.flutter_mapbox.pigeons._OfflineSwitch

class OfflineSwitch : _OfflineSwitch {

  private val switcher = com.mapbox.common.OfflineSwitch.getInstance()

  override fun setMapboxStackConnected(connected: Boolean) {
    switcher.isMapboxStackConnected = connected
  }

  override fun isMapboxStackConnected(): Boolean {
    return switcher.isMapboxStackConnected
  }
}