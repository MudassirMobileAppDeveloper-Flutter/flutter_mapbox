package com.nick92.flutter_mapbox.snapshot

import android.annotation.SuppressLint
import android.content.Context
import com.mapbox.maps.MapboxStyleManager
import com.mapbox.maps.Snapshotter
import com.nick92.flutter_mapbox.MapboxEventHandler
import com.nick92.flutter_mapbox.StyleController
import com.nick92.flutter_mapbox.pigeons.MapSnapshotOptions
import com.nick92.flutter_mapbox.pigeons.StyleManager
import com.nick92.flutter_mapbox.pigeons._SnapshotterInstanceManager
import com.nick92.flutter_mapbox.pigeons._SnapshotterMessenger
import com.nick92.flutter_mapbox.styleManager
import com.nick92.flutter_mapbox.toSnapshotOptions
import com.nick92.flutter_mapbox.toSnapshotOverlayOptions
import io.flutter.plugin.common.BinaryMessenger

class SnapshotterInstanceManager(
  private val context: Context,
  private val messenger: BinaryMessenger,
) : _SnapshotterInstanceManager {

  @SuppressLint("RestrictedApi")
  override fun setupSnapshotterForSuffix(
    suffix: String,
    eventTypes: List<Long>,
    options: MapSnapshotOptions
  ) {
    val snapshotter = Snapshotter(
      context,
      options = options.toSnapshotOptions(context),
      overlayOptions = options.toSnapshotOverlayOptions()
    )
    val styleManager: com.mapbox.maps.StyleManager = snapshotter.styleManager() // TODO: expose this on Android
    val eventHandler = MapboxEventHandler(styleManager, messenger, eventTypes.map { it }, suffix)
    val snapshotterController = SnapshotterController(context, snapshotter, eventHandler)
    val mapboxStyleManager = MapboxStyleManager(
      styleManager,
      options.pixelRatio.toFloat(),
      mapLoadingErrorDelegate = {}
    )
    val snapshotterStyleController = StyleController(context, mapboxStyleManager)

    _SnapshotterMessenger.setUp(messenger, snapshotterController, suffix)
    StyleManager.setUp(messenger, snapshotterStyleController, suffix)
  }

  override fun tearDownSnapshotterForSuffix(suffix: String) {
    _SnapshotterMessenger.setUp(messenger, null, suffix)
    StyleManager.setUp(messenger, null, suffix)
  }
}