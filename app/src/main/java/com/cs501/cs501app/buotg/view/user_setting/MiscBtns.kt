package com.cs501.cs501app.buotg.view.user_setting

import android.content.Intent
import android.location.Location
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.cs501.cs501app.R
import com.cs501.cs501app.buotg.database.entities.KVEntry
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.common.Ping
import com.cs501.cs501app.buotg.view.homeScreen.POLL_STATE_KEY
import com.cs501.cs501app.buotg.view.user_map.TestAddrPickerActivity
import com.cs501.cs501app.buotg.view.user_map.getCurrentLocation
import com.cs501.cs501app.utils.*
import kotlinx.coroutines.launch

@Composable
fun ComposedMiscBtns(){
    val ctx = LocalContext.current
    Row(){
        Ping(modifier = Modifier.padding(8.dp))
    }
    Row(){
        TogglePollButton()
    }
    Row {
        ToggleBackgroundServiceBtn()
    }
}

@Composable
fun TestNotification(){
    val ctx = LocalContext.current
    Button(onClick = {
        sendNotification(ctx, "* * * wants to:", "know your location")
    }) {
        Text("send notification")
    }
}

@Composable
fun GetLocation(){
    val ctx = LocalContext.current
    fun handleLocation(location: Location?){
        println("location: $location")
    }
    Button(onClick = {

        val res = getCurrentLocation(ctx) { location: Location? -> handleLocation(location) }
        println("res: $res")
    }) {
        Text("get location")
    }
}



@Composable
fun TogglePollButton(){
    val isPolling = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val kvDao = AppRepository.get().kvDao()
    val ctx = LocalContext.current

    LaunchedEffect(true){
        kvDao.get(POLL_STATE_KEY)?.let {
            val v = it.value
            isPolling.value = v == "true"
        }
    }

    Text(stringResource(id = R.string.poll_toggle))
    Switch(
        checked = isPolling.value,
        onCheckedChange = {
            isPolling.value = it
            val v = if(it){"true"}else{"false"}
            val kvEntry = KVEntry(POLL_STATE_KEY, v)
            coroutineScope.launch {
                kvDao.put(kvEntry)
                setupBackgroundWork(ctx)
            }
        })
}

@Composable
fun ToggleBackgroundServiceBtn(){
    val ctx = LocalContext.current
    val serviceOn = remember { mutableStateOf(false) }
    val btnTxt = stringResource(id = R.string.bg_svc_toggle)
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(true){
        serviceOn.value = getBGserviceState()
    }
    Text(btnTxt)
    Switch(checked=serviceOn.value, onCheckedChange = {
        serviceOn.value = it
        coroutineScope.launch {
            setBGServiceState(ctx, it)
        }
//        val _action = if(it){
//            BGService.ACTION_START}else{
//            BGService.ACTION_STOP}
//        val intent = Intent(ctx, BGService::class.java).apply {
//            action = _action
//        }
//        ctx.startService(intent)
    })
}

