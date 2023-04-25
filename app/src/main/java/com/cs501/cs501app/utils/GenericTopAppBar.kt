package com.cs501.cs501app.utils

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cs501.cs501app.buotg.CustomText

@Composable
fun GenericTopAppBar(title:String = "BU On-the-go",
                     onNavigationIconClick: () -> Unit,
                     hasNavMenu:Boolean=false,
                     finished:()->Unit={},
) {
    TopAppBar(
        title = {
            CustomText(text = title,fontSize = 20.sp,fontWeight = FontWeight.Bold)
        },
        backgroundColor = Color.White,
        navigationIcon = {
            if(hasNavMenu){
                IconButton(onClick = onNavigationIconClick) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Toggle drawer"
                    )
                }
            } else {
                IconButton(onClick = {
                    finished()
                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Go back"
                    )
                }
            }
        }
    )
}