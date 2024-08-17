// swift-tools-version: 6.0
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let package = Package(
    name: "iosLib",
    platforms: [
            .iOS(.v18),
            .tvOS(.v18),
            .watchOS(.v11),
            .macOS(.v15)
        ],
    products: [
        // Products define the executables and libraries a package produces, making them visible to other packages.
        .library(
            name: "iosLib",
            targets: ["iosLib", "clients"]
        ),
    ],
    targets: [
        // Targets are the basic building blocks of a package, defining a module or a test suite.
        // Targets can depend on other targets in this package and products from dependencies.
        .target(
            name: "iosLib",
            dependencies: ["clients"]
        ),
        .binaryTarget(name: "clients", path: "../clients/build/XCFrameworks/debug/clients.xcframework"),
        .testTarget(
            name: "iosLibTests",
            dependencies: ["iosLib"]
        ),
    ]
)
