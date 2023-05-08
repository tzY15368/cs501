package com.cs501.cs501app.buotg.view.user_setting

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.CustomButton
import com.cs501.cs501app.buotg.CustomTextField
import com.cs501.cs501app.buotg.database.entities.User
import com.cs501.cs501app.buotg.database.entities.UserType
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoEdit(done: () -> Unit = {}, context: Context) {
    val userRepo = AppRepository.get().userRepo()
    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current
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
                            userRepo.updateUser(ctx, updatedUser)
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
    val thisUser = user.copy()
    val full_name = remember { mutableStateOf(thisUser.full_name) }
    val email = remember { mutableStateOf(thisUser.email) }
    var typeMenuExpanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(thisUser.user_type) }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row {
            CustomTextField(
                value = full_name.value,
                onValueChange = { full_name.value = it },
                stringResource(id = R.string.name)
            )
        }
        Row {
            CustomTextField(
                value = email.value,
                onValueChange = { email.value = it },
                stringResource(id = R.string.email)
            )
        }
        Row {
            ExposedDropdownMenuBox(
                expanded = typeMenuExpanded,
                onExpandedChange = { typeMenuExpanded = !typeMenuExpanded }) {
                CustomTextField(
                    value = selectedType.toString(),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeMenuExpanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = typeMenuExpanded,
                    onDismissRequest = { typeMenuExpanded = false }) {
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.type_student)) },
                        onClick = {
                            typeMenuExpanded = false
                            selectedType = UserType.student
                        },
                    )
                    Divider()
                    DropdownMenuItem(
                        text = { Text(stringResource(id = R.string.type_teacher)) },
                        onClick = {
                            typeMenuExpanded = false
                            selectedType = UserType.teacher
                        })
                }
            }
        }
        Row {
            CustomButton(onClick = {
                // Create a new user object with the updated info
                val updatedUser = User(
                    created_at = thisUser.created_at,
                    full_name = full_name.value,
                    email = email.value,
                    user_type = selectedType,
                    user_id = thisUser.user_id
                )
                // Call the onUserInfoUpdated callback to notify the caller of the updated user info
                onUserInfoUpdated(updatedUser)
            }, text = stringResource(id = R.string.save_2))
        }
    }
}