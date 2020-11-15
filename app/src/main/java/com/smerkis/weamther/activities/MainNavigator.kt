package com.smerkis.weamther.activities

import androidx.navigation.NavDirections

interface MainNavigator {
    fun navigateTo(layout: Layout)
    fun navigateTo(directions: NavDirections)
    fun closeApp()
}