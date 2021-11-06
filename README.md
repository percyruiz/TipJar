# TipJar

This application implements tip computation and receipt details saving features based on the ff: design https://www.figma.com/file/Wjc3WG4kH8IFooV2DLv29v/TipJar?node-id=0%3A1  

Implemented features are:
   - Adding Tips (computations, taking photo, local db saving)
   - Viewing Receipts (getting receipts list from local db)
   - Searching Receipts (date range)
   - Deleting Receipts (swipe to delete receipt item)

## Installation
Clone this repository and import into **Android Studio**
```bash
https://github.com/percyruiz/TipJar.git
```

## Screenshots
<p align="center">
  <img src="https://user-images.githubusercontent.com/16864893/140605175-ba564f38-44a0-4da7-9637-6abd66a55bb3.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://user-images.githubusercontent.com/16864893/140605216-d7b7e4ed-c4c2-48ec-96a0-d654aec15913.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>
  <img src="https://user-images.githubusercontent.com/16864893/140605307-65bc193a-863e-4272-b03d-919365f9ca07.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://user-images.githubusercontent.com/16864893/140605326-aacca68d-8a15-4992-8527-d36510721da9.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>
  <img src="https://user-images.githubusercontent.com/16864893/140605435-b94eaac4-8d8d-458f-939e-31a349468b7c.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://user-images.githubusercontent.com/16864893/140605550-5669de3d-ed5a-401e-b57b-4c7711f107a2.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<br/>
  <img src="https://user-images.githubusercontent.com/16864893/140605597-6ba87f10-ca7f-4427-8b31-490600dde21a.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <img src="https://user-images.githubusercontent.com/16864893/140605577-a6528e2f-516c-4ebd-a67b-787243d9bb95.png" width="25%" height="25%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</p>

## Architecture
The app uses MVVM architecture which takes advantage of Android Jetpack's Android Architecture Components https://developer.android.com/topic/libraries/architecture.
<p align="center">
  <img src="https://user-images.githubusercontent.com/16864893/126056079-2c0e8155-2201-45e6-bf3f-514eda1c39ff.png" width="50%" height="50%"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</p>

## Dependencies
- Dependency injection (with [Koin](https://www.koin.com/))
- Reactive programming (with [Kotlin Flows](https://kotlinlang.org/docs/reference/coroutines/flow.html))
- Paging implementation (with [Paging3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview))
- Google [Material Design](https://material.io/blog/android-material-theme-color) library
- Android architecture components
- JSR-310 backport for Android (with https://github.com/JakeWharton/ThreeTenABP)
- Image Loader (with https://github.com/bumptech/glide)

## Maintainers
This project is mantained by:
* [Percival Ruiz](https://github.com/percyruiz)
