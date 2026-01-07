package com.nick92.flutter_mapbox.snapshot

import android.content.Context
import android.graphics.Bitmap
import com.mapbox.geojson.Point
import com.mapbox.maps.Snapshotter
import com.nick92.flutter_mapbox.MapboxEventHandler
import com.nick92.flutter_mapbox.pigeons.CameraOptions
import com.nick92.flutter_mapbox.pigeons.CameraState
import com.nick92.flutter_mapbox.pigeons.CanonicalTileID
import com.nick92.flutter_mapbox.pigeons.CoordinateBounds
import com.nick92.flutter_mapbox.pigeons.MbxEdgeInsets
import com.nick92.flutter_mapbox.pigeons.TileCoverOptions
import com.nick92.flutter_mapbox.pigeons._SnapshotterMessenger
import com.nick92.flutter_mapbox.toCameraOptions
import com.nick92.flutter_mapbox.toCameraState
import com.nick92.flutter_mapbox.toEdgeInsets
import com.nick92.flutter_mapbox.toFLTCameraOptions
import com.nick92.flutter_mapbox.toFLTCanonicalTileID
import com.nick92.flutter_mapbox.toFLTCoordinateBounds
import com.nick92.flutter_mapbox.toFLTSize
import com.nick92.flutter_mapbox.toSize
import com.nick92.flutter_mapbox.toTileCoverOptions
import java.io.ByteArrayOutputStream

class SnapshotterController(
  private val context: Context,
  private val snapshotter: Snapshotter,
  private val eventHandler: MapboxEventHandler
) : _SnapshotterMessenger {
  override fun getSize(): com.nick92.flutter_mapbox.pigeons.Size {
    return snapshotter.getSize().toFLTSize(context)
  }

  override fun setSize(size: com.nick92.flutter_mapbox.pigeons.Size) {
    snapshotter.setSize(size.toSize(context))
  }

  override fun getCameraState(): CameraState {
    return snapshotter.getCameraState().toCameraState(context)
  }

  override fun setCamera(cameraOptions: CameraOptions) {
    snapshotter.setCamera(cameraOptions.toCameraOptions(context))
  }

  override fun start(callback: (Result<ByteArray?>) -> Unit) {
    snapshotter.start(null) { snapshot, error ->
      if (snapshot != null) {
        val byteArray = ByteArrayOutputStream().also { stream ->
          snapshot.compress(Bitmap.CompressFormat.PNG, 100, stream)
        }.toByteArray()

        callback(Result.success(byteArray))
      } else {
        callback(Result.failure(Throwable(error ?: "Unknown error")))
      }
    }
  }

  override fun cancel() {
    snapshotter.cancel()
  }

  override fun coordinateBounds(camera: CameraOptions): CoordinateBounds {
    return snapshotter.coordinateBoundsForCamera(camera.toCameraOptions(context)).toFLTCoordinateBounds()
  }

  override fun camera(
    coordinates: List<Point>,
    padding: MbxEdgeInsets?,
    bearing: Double?,
    pitch: Double?
  ): CameraOptions {
    // TODO: remove default values from padding, bearing and pitch after they are optional on Android
    return snapshotter.cameraForCoordinates(
      coordinates,
      (padding ?: MbxEdgeInsets(0.0, 0.0, 0.0, 0.0)).toEdgeInsets(context),
      bearing ?: 0.0,
      pitch ?: 0.0
    )
      .toFLTCameraOptions(context)
  }

  override fun tileCover(options: TileCoverOptions): List<CanonicalTileID> {
    return snapshotter.tileCover(options.toTileCoverOptions(), null).map { it.toFLTCanonicalTileID() }
  }

  override fun clearData(callback: (Result<Unit>) -> Unit) {
    Snapshotter.clearData() {
      if (it.isError) {
        callback(Result.failure(Throwable(it.error)))
      } else {
        callback(Result.success(Unit))
      }
    }
  }
}