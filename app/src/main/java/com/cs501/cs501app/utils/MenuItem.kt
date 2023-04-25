package com.cs501.cs501app.utils

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItem(
    val id: Int,
    val title: String,
    val contentDescription: String,
    val icon: ImageVector
)