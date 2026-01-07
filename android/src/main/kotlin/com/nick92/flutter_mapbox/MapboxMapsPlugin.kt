package com.nick92.flutter_mapbox

import androidx.lifecycle.Lifecycle
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.embedding.engine.plugins.lifecycle.FlutterLifecycleAdapter

class MapboxMapsPlugin : FlutterPlugin, ActivityAware {

  private var lifecycle: Lifecycle? = null

  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    // This connects the Dart "plugins.flutter.io/mapbox_maps" to the native View
    binding.platformViewRegistry.registerViewFactory(
        "plugins.flutter.io/mapbox_maps",
        MapboxMapFactory(
            binding.binaryMessenger,
            binding.flutterAssets,
            object : LifecycleProvider {
                override fun getLifecycle(): Lifecycle? = lifecycle
            }
        )
    )
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    lifecycle = null
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    lifecycle = FlutterLifecycleAdapter.getActivityLifecycle(binding)
  }

  override fun onDetachedFromActivity() {
    lifecycle = null
  }

  override fun onDetachedFromActivityForConfigChanges() {
    lifecycle = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    lifecycle = FlutterLifecycleAdapter.getActivityLifecycle(binding)
  }

  interface LifecycleProvider {
    fun getLifecycle(): Lifecycle?
  }
}