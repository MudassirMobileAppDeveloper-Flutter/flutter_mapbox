import 'package:flutter/material.dart';
import 'package:flutter_mapbox_example/animated_route_example.dart';
import 'package:flutter_mapbox_example/animation_example.dart';
import 'package:flutter_mapbox_example/camera_example.dart';
import 'package:flutter_mapbox_example/circle_annotations_example.dart';
import 'package:flutter_mapbox_example/cluster_example.dart';
import 'package:flutter_mapbox_example/custom_vector_icons_example.dart';
import 'package:flutter_mapbox_example/custom_header_example.dart';
import 'package:flutter_mapbox_example/draggable-annotations-example.dart';
import 'package:flutter_mapbox_example/edit_polygon_example.dart';
import 'package:flutter_mapbox_example/model_layer_interactions_example.dart';
import 'package:flutter_mapbox_example/offline_map_example.dart';
import 'package:flutter_mapbox_example/model_layer_example.dart';
import 'package:flutter_mapbox_example/ornaments_example.dart';
import 'package:flutter_mapbox_example/geojson_line_example.dart';
import 'package:flutter_mapbox_example/image_source_example.dart';
import 'package:flutter_mapbox_example/map_interface_example.dart';
import 'package:flutter_mapbox_example/standard_style_import_example.dart';
import 'package:flutter_mapbox_example/standard_style_interactions_example.dart';
import 'package:flutter_mapbox_example/polygon_annotations_example.dart';
import 'package:flutter_mapbox_example/polyline_annotations_example.dart';
import 'package:flutter_mapbox_example/simple_map_example.dart';
import 'package:flutter_mapbox_example/snapshotter_example.dart';
import 'package:flutter_mapbox_example/traffic_layer_example.dart';
import 'package:flutter_mapbox_example/spinning_globe_example.dart';
import 'package:flutter_mapbox_example/traffic_route_line_example.dart';
import 'package:flutter_mapbox_example/tile_json_example.dart';
import 'package:flutter_mapbox_example/vector_tile_source_example.dart';
import 'package:flutter_mapbox_example/viewport_example.dart';
import 'package:flutter_mapbox/flutter_mapbox.dart';

import 'full_map_example.dart';
import 'location_example.dart';
import 'example.dart';
import 'point_annotations_example.dart';
import 'projection_example.dart';
import 'style_example.dart';
import 'gestures_example.dart';
import 'debug_options_example.dart';
import 'map_recorder_example.dart';

final List<Example> _allPages = <Example>[
  SimpleMapExample(),
  ViewportExample(),
  SnapshotterExample(),
  TrafficRouteLineExample(),
  OfflineMapExample(),
  ModelLayerExample(),
  DebugOptionsExample(),
  SpinningGlobeExample(),
  StandardStyleImportExample(),
  StandardStyleInteractionsExample(),
  ModelLayerInteractionsExample(),
  FullMapExample(),
  StyleExample(),
  CameraExample(),
  ProjectionExample(),
  MapInterfaceExample(),
  StyleClustersExample(),
  AnimationExample(),
  MapRecorderExample(),
  PointAnnotationExample(),
  CircleAnnotationExample(),
  PolylineAnnotationExample(),
  PolygonAnnotationExample(),
  DraggableAnnotationExample(),
  EditPolygonExample(),
  VectorTileSourceExample(),
  DrawGeoJsonLineExample(),
  ImageSourceExample(),
  TileJsonExample(),
  LocationExample(),
  GesturesExample(),
  OrnamentsExample(),
  AnimatedRouteExample(),
  CustomHeaderExample(),
  TrafficLayerExample(),
  CustomVectorIconsExample(),
];

class MapsDemo extends StatelessWidget {
  // Use the same hardcoded token to bypass the red warning screen in your UI
  static const String ACCESS_TOKEN =
      "pk.eyJ1IjoibW9kYXNzaXIiLCJhIjoiY21rNDVxaWlqMDJ2ZDNkcjVrdnlpeXk0NCJ9.lJzfzyiJttWthIZEc74AXA";

  const MapsDemo({super.key});

  void _pushPage(BuildContext context, Example page) {
    Navigator.of(context).push(MaterialPageRoute<void>(
        builder: (_) => Scaffold(
              appBar: AppBar(title: Text(page.title)),
              body: page,
            )));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('MapboxMaps examples')),
      body: ACCESS_TOKEN.isEmpty || ACCESS_TOKEN.contains("ADD_YOUR_TOKEN")
          ? buildAccessTokenWarning()
          : ListView.separated(
              itemCount: _allPages.length,
              separatorBuilder: (_, __) => const Divider(height: 1),
              itemBuilder: (_, int index) {
                final example = _allPages[index];
                return ListTile(
                  leading: example.leading,
                  title: Text(example.title),
                  subtitle: (example.subtitle?.isNotEmpty == true)
                      ? Text(example.subtitle!,
                          maxLines: 2, overflow: TextOverflow.ellipsis)
                      : null,
                  onTap: () => _pushPage(context, example),
                );
              },
            ),
    );
  }

  Widget buildAccessTokenWarning() {
    return Container(
      color: Colors.red[900],
      child: Center(
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Text(
            "Access Token is missing or invalid.\nPlease check your main.dart configuration.",
            textAlign: TextAlign.center,
            style: TextStyle(color: Colors.white, fontWeight: FontWeight.bold),
          ),
        ),
      ),
    );
  }
}

void main() {
  // 1. Initialize the Flutter binding to enable platform channel communication
  WidgetsFlutterBinding.ensureInitialized();

  // 2. Set your access token directly
  // Note: Your token is already used here to ensure native initialization
  const String myToken =
      "pk.eyJ1IjoibW9kYXNzaXIiLCJhIjoiY21rNDVxaWlqMDJ2ZDNkcjVrdnlpeXk0NCJ9.lJzfzyiJttWthIZEc74AXA";
  MapboxOptions.setAccessToken(myToken);

  runApp(MaterialApp(
    home: MapsDemo(),
    debugShowCheckedModeBanner: false,
  ));
}
