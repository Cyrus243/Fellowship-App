package com.indelible.fellowship.navigation

sealed class ImageViewerScreens(
    val imagePathArgs: String,
    val route: String
){
    data object ImageViewerScreen: ImageViewerScreens(
        imagePathArgs = "/{imagePath}",
        route = "image_viewer"
    ){
        val fullRoute = route + imagePathArgs
    }
}