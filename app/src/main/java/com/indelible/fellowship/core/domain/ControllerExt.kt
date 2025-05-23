package com.indelible.fellowship.core.domain

import androidx.navigation.NavController

fun NavController.navigateAndPopUp(route: Any, popUp: Any){
    this.navigate(route){
        launchSingleTop = true
        popUpTo(popUp){
            inclusive = true
        }
    }
}