package com.cs501.cs501app.buotg.view.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.User

@Composable
fun UserCardView(User: User) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val name = stringResource(id = R.string.name)
            val email = stringResource(id = R.string.email)
            Text(text = "$name:" + User.full_name)
            Text(text = "$email:" + User.email)
        }
    }
}