package com.smerkis.weamther.activities

interface MainNavigator {
    fun navigateTo(layout: Layout)
    fun closeApp()
    fun toast(msg: String)
}