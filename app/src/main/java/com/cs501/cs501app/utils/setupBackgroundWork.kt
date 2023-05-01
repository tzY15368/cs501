package com.cs501.cs501app.utils

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.cs501.cs501app.buotg.database.repositories.AppRepository
import com.cs501.cs501app.buotg.view.homeScreen.POLL_STATE_KEY
import com.cs501.cs501app.buotg.view.homeScreen.WORK_NAME
import java.util.concurrent.TimeUnit

suspend fun setupBackgroundWork(ctx: Context){
    val kvDao = AppRepository.get().kvDao()
    kvDao.get(POLL_STATE_KEY)?.let {
        if(it.value == "true"){
            val periodicRequest = PeriodicWorkRequestBuilder<PollWorker>(15, TimeUnit.MINUTES)
                .build()
            WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                periodicRequest
            )
            Log.d("HomeActivity", "Polling is enabled")
        }else{
            WorkManager.getInstance(ctx).cancelUniqueWork(WORK_NAME)
            Log.d("HomeActivity", "Polling is disabled")
        }
    }
}