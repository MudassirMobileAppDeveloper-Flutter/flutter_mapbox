// swift-tools-version: 5.9
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "flutter_mapbox",
    platforms: [
        .iOS("14.0")
    ],
    products: [
        .library(name: "mapbox-maps-flutter", targets: ["flutter_mapbox"])
    ],
    dependencies: [
        .package(url: "https://github.com/mapbox/mapbox-maps-ios.git", exact: "11.18.0-beta.1")
    ],
    targets: [
        .target(
            name: "flutter_mapbox",
            dependencies: [
                .product(name: "MapboxMaps", package: "mapbox-maps-ios")
            ],
            resources: []
        )
    ]
)
