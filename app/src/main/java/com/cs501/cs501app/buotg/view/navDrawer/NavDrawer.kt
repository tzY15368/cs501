package com.cs501.cs501app.buotg.view.navDrawer

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.cs501.cs501app.buotg.view.homeScreen.HomeActivity
import com.cs501.cs501app.buotg.view.thirdParty.chatRoom.ChatRoomActivity
import com.cs501.cs501app.buotg.view.user_group.StudyGroupActivity
import com.cs501.cs501app.buotg.view.user_invite.InviteActivity
import com.cs501.cs501app.buotg.view.user_map.MapViewActivity
import com.cs501.cs501app.buotg.view.user_setting.SettingActivity
import com.cs501.cs501app.buotg.view.user_setup.SetupActivity

@Composable
fun NavDrawer(){
    val ctx = LocalContext.current
    DrawerHeader()
    DrawerBody(
        items = listOf(
            MenuItem(
                title = "Home",
                contentDescription = "Goto home",
                icon = Icons.Default.Home,
                bindClass = HomeActivity::class.java
            ),
            MenuItem(
                title = "Setup",
                contentDescription = "Goto setup",
                icon = Icons.Default.AccountBox,
                bindClass = SetupActivity::class.java
            ),
            MenuItem(
                title = "Study Group",
                contentDescription = "Goto Study Group",
                icon = Icons.Default.Person,
                bindClass = StudyGroupActivity::class.java
            ),
            MenuItem(
                title = "Map View",
                contentDescription = "Goto Map View",
                icon = Icons.Default.LocationOn,
                bindClass = MapViewActivity::class.java
            ),
            MenuItem(
                title = "Invite",
                contentDescription = "Goto My Invite",
                icon = Icons.Default.Info,
                bindClass = InviteActivity::class.java
            ),
            MenuItem(
                title = "Chat Room",
                contentDescription = "Goto Chat Room (Groups)",
                icon = Icons.Default.Email,
                bindClass = ChatRoomActivity::class.java
            ),
            MenuItem(
                title = "Setting",
                contentDescription = "Goto setting",
                icon = Icons.Default.Settings,
                bindClass = SettingActivity::class.java
            ),
        ),
        onItemClick = {
            println("Clicked on ${it.title}")
            val intent = Intent(ctx, it.bindClass)
            ContextCompat.startActivity(ctx, intent, null)
        },
    )
}