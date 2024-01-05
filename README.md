# KMM BLE Library for mobile

WIP

This is a Kotlin Multiplatform project for Android and iOS. It provides an API for using Bluetooth
Low Energy. The API is a shared part from both platforms. 

## Library
The repository contains a Kotlin Multiplatform BLE library which unifies BLE API between Android
and iOS. Android part it is a wrapper around [BLEK](https://github.com/NordicSemiconductor/Kotlin-BLE-Library).
iOS part has been implemented in Kotlin Multiplatform and uses native iOS classes like 
CBPeripheralManager and CBCentralManager.

## Example App
The repository contains also an example app based on [the Blinky profile](https://github.com/NordicSemiconductor/Android-nRF-Blinky). 
The app uses Compose Multiplatform and shows the usage of the library. iOS project should be run 
using Xcode, by opening iosApp directory.
