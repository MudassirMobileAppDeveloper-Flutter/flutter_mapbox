package com.nick92.flutter_mapbox.annotation

import com.mapbox.maps.plugin.annotation.AnnotationManager

interface ControllerDelegate {
  fun getManager(managerId: String): AnnotationManager<*, *, *, *, *, *, *>
}