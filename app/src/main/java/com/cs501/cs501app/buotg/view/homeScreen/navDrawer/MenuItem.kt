package com.cs501.cs501app.buotg.view.homeScreen.navDrawer

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val title: String,
    val contentDescription: String,
    val icon: ImageVector,
    val bindClass: Class<*>
)