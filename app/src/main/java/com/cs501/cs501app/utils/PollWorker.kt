package com.cs501.cs501app.utils

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cs501.cs501app.buotg.view.user_map.getCurrentLocation

private const val TAG = "PollWorker"
class PollWorker(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    fun handleLocationUpdate(loc: Location?) {
        Log.i(TAG, "Current location: $loc")
    }

    override suspend fun doWork(): Result {
        Log.i(TAG, "Work request triggered")
        val res = getCurrentLocation(context) { handleLocationUpdate(it) }
        Log.i(TAG, "Work request completed: $res")
        return if(res.isSuccess) Result.success() else Result.failure()
    }
}