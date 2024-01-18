// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "KMM-BLE-Library",
    platforms: [
        .iOS(.v13)
    ],
    products: [
        .library(
            name: "KMM-BLE-Library",
            targets: ["KMM-BLE-Library"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "KMM-BLE-Library",
        ),
    ]
)
