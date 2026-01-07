package com.nick92.flutter_mapbox.offline

import android.content.Context
import android.os.Handler
import com.mapbox.common.TileStore
import com.mapbox.maps.OfflineManager
import com.nick92.flutter_mapbox.pigeons.*
import com.nick92.flutter_mapbox.toFLTTileRegion
import com.nick92.flutter_mapbox.toFLTTileRegionEstimateProgress
import com.nick92.flutter_mapbox.toFLTTileRegionEstimateResult
import com.nick92.flutter_mapbox.toFLTTileRegionLoadProgress
import com.nick92.flutter_mapbox.toFLTValue
import com.nick92.flutter_mapbox.toGeometry
import com.nick92.flutter_mapbox.toNetworkRestriction
import com.nick92.flutter_mapbox.toResult
import com.nick92.flutter_mapbox.toTileDataDomain
import com.nick92.flutter_mapbox.toTileRegionEstimateOptions
import com.nick92.flutter_mapbox.toTileStoreOptionsKey
import com.nick92.flutter_mapbox.toTilesetDescriptorOptions
import com.nick92.flutter_mapbox.toValue
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel

private const val EVENT_CHANNEL_PREFIX = "com.mapbox.maps.flutter/tilestore"

class TileStoreController(
  private val context: Context,
  private val binaryMessenger: BinaryMessenger,
  private val channelSuffix: String,
  private val tileStore: TileStore
) : _TileStore {

  private val offlineManager = OfflineManager()
  private var tileRegionLoadProgressHandlers = HashMap<String, EventChannel.EventSink>()
  private var tileRegionEstimateProgressHandlers = HashMap<String, EventChannel.EventSink>()
  private val mainHandler = Handler(context.mainLooper)

  override fun loadTileRegion(
    id: String,
    loadOptions: TileRegionLoadOptions,
    callback: (Result<TileRegion>) -> Unit
  ) {
    tileStore.loadTileRegion(
      id, offlineManager.tileRegionLoadOptions(loadOptions, context),
      { progress ->
        mainHandler.post {
          tileRegionLoadProgressHandlers[id]?.success(progress.toFLTTileRegionLoadProgress().toList())
        }
      },
      { expected ->
        mainHandler.post {
          callback(expected.toResult { it.toFLTTileRegion() })
          tileRegionLoadProgressHandlers.remove(id)?.endOfStream()
        }
      }
    )
  }

  override fun addTileRegionLoadProgressListener(id: String) {
    val eventChannel = EventChannel(binaryMessenger, "com.mapbox.maps.flutter/$channelSuffix/tile-region-$id")
    eventChannel.setStreamHandler(
      object : EventChannel.StreamHandler {
        override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
          events?.let { tileRegionLoadProgressHandlers[id] = it }
        }
        override fun onCancel(arguments: Any?) {
          tileRegionLoadProgressHandlers.remove(id)
        }
      }
    )
  }

  override fun estimateTileRegion(
    id: String,
    loadOptions: TileRegionLoadOptions,
    estimateOptions: TileRegionEstimateOptions?,
    callback: (Result<TileRegionEstimateResult>) -> Unit
  ) {
    tileStore.estimateTileRegion(
      id,
      offlineManager.tileRegionLoadOptions(loadOptions, context),
      estimateOptions?.toTileRegionEstimateOptions() ?: com.mapbox.common.TileRegionEstimateOptions(null),
      { progress ->
        mainHandler.post {
          tileRegionEstimateProgressHandlers[id]?.success(
            progress.toFLTTileRegionEstimateProgress().toList()
          )
        }
      },
      { expected ->
        mainHandler.post {
          callback(expected.toResult { it.toFLTTileRegionEstimateResult() })
          tileRegionEstimateProgressHandlers.remove(id)?.endOfStream()
        }
      }
    )
  }

  override fun addTileRegionEstimateProgressListener(id: String) {
    val eventChannel = EventChannel(binaryMessenger, "com.mapbox.maps.flutter/$channelSuffix/tile-region-estimate$id")
    eventChannel.setStreamHandler(
      object : EventChannel.StreamHandler {
        override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
          events?.let { tileRegionEstimateProgressHandlers[id] = it }
        }
        override fun onCancel(arguments: Any?) {
          tileRegionEstimateProgressHandlers.remove(id)
        }
      }
    )
  }

  override fun tileRegionMetadata(id: String, callback: (Result<Map<String, Any>>) -> Unit) {
    tileStore.getTileRegionMetadata(id) { expected ->
      mainHandler.post {
        callback(expected.toResult { it.toFLTValue() as? Map<String, Any> ?: emptyMap() })
      }
    }
  }

  override fun tileRegionContainsDescriptor(id: String, options: List<TilesetDescriptorOptions>, callback: (Result<Boolean>) -> Unit) {
    val descriptors = options.map { offlineManager.createTilesetDescriptor(it.toTilesetDescriptorOptions(context)) }
    tileStore.tileRegionContainsDescriptors(id, descriptors) { expected ->
      mainHandler.post {
        callback(expected.toResult { it })
      }
    }
  }

  override fun allTileRegions(callback: (Result<List<TileRegion>>) -> Unit) {
    tileStore.getAllTileRegions { expected ->
      mainHandler.post {
        callback(expected.toResult { it.map { region -> region.toFLTTileRegion() } })
      }
    }
  }

  override fun tileRegion(id: String, callback: (Result<TileRegion>) -> Unit) {
    tileStore.getTileRegion(id) { expected ->
      mainHandler.post {
        callback(expected.toResult { it.toFLTTileRegion() })
      }
    }
  }

  override fun removeRegion(id: String, callback: (Result<TileRegion>) -> Unit) {
    tileStore.removeTileRegion(
      id,
      { expected ->
        mainHandler.post {
          callback(expected.toResult { it.toFLTTileRegion() })
        }
      }
    )
  }

  override fun setOptionForKey(key: _TileStoreOptionsKey, domain: TileDataDomain?, value: Any?) {
    domain?.also {
      tileStore.setOption(key.toTileStoreOptionsKey(), it.toTileDataDomain(), value?.toValue() ?: com.mapbox.bindgen.Value.nullValue())
    } ?: run {
      tileStore.setOption(key.toTileStoreOptionsKey(), value?.toValue() ?: com.mapbox.bindgen.Value.nullValue())
    }
  }
}

private fun OfflineManager.tileRegionLoadOptions(fltValue: TileRegionLoadOptions, context: Context): com.mapbox.common.TileRegionLoadOptions {
  val builder = com.mapbox.common.TileRegionLoadOptions.Builder()
    .geometry(fltValue.geometry?.toGeometry())
    .metadata(fltValue.metadata?.toValue())
    .acceptExpired(fltValue.acceptExpired)
    .networkRestriction(fltValue.networkRestriction.toNetworkRestriction())
    .startLocation(fltValue.startLocation)
    .averageBytesPerSecond(fltValue.averageBytesPerSecond?.toInt())
    .extraOptions(fltValue.extraOptions?.toValue())

  fltValue.descriptorsOptions?.let { options ->
    val descriptors: List<TilesetDescriptorOptions> = options.filterNotNull()
    builder.descriptors(
      descriptors.map { createTilesetDescriptor(it.toTilesetDescriptorOptions(context)) }
    )
  }

  return builder.build()
}