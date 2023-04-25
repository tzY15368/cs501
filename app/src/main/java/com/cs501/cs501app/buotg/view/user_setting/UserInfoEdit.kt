package com.cs501.cs501app.buotg.view.user_setting

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.entities.UserType
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.utils.GenericTopAppBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoEdit(done: () -> Unit = {}, context: Context) {
    val userRepo = AppRepository.get().userRepo()
    val coroutineScope = rememberCoroutineScope()
    val currentUser = remember { mutableStateOf<User?>(null) }
//    val currentUser = User(
//        UUID.randomUUID(),
//        "example@example.com",
//        "John Doe",
//        Date(),
//        UserType.student
//    )
    //Fetch the current user info
    LaunchedEffect(key1 = Unit) {
        currentUser.value = withContext(Dispatchers.IO) {
            userRepo.getCurrentUser()
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        if (currentUser.value == null) {
            Text(stringResource(id = R.string.please_login))
        } else {
            val text = stringResource(id = R.string.user_update)
            currentUser.value?.let { user ->
                // Show the user's info in a form
                UserInfoForm(user = user) { updatedUser ->
                    // Update the user info in the database
                    coroutineScope.launch {
                        withContext(Dispatchers.IO) {
                            userRepo.updateUser(updatedUser)
                        }
                        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                        done()
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoForm(user: User, onUserInfoUpdated: (User) -> Unit) {
    val full_name = remember { mutableStateOf(user.full_name) }
    val email = remember { mutableStateOf(user.email) }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = full_name.value,
            onValueChange = { full_name.value = it },
            label = { Text(stringResource(id = R.string.name)) },
            singleLine = true
        )

        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text(stringResource(id = R.string.email)) },
            singleLine = true
        )


        Button(
            onClick = {
                // Create a new user object with the updated info
                val updatedUser = User(
                    created_at = user.created_at,
                    full_name = full_name.value,
                    email = email.value,
                    user_type = user.user_type,
                    user_id = user.user_id
                )

                // Call the onUserInfoUpdated callback to notify the caller of the updated user info
                onUserInfoUpdated(updatedUser)
            }
        ) {
            Text(stringResource(id = R.string.save_2))
        }
    }
}