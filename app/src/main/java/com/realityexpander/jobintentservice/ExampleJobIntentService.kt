package com.realityexpander.jobintentservice

import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import androidx.core.app.JobIntentService

// The work Starts as soon as possible, does not have any implicit constraints.
// This is meant to mimic the IntentService behavior.
class ExampleJobIntentService : JobIntentService() {
    var isJobCancelled = false

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "onHandleWork")
        val input = intent.getStringExtra("inputExtra")

        // If this job takes longer than 10 minutes, it may be cancelled, and then
        //   restarted at a later time, and the same `intent` will be sent again.
        // This allows the work to resume if needed.
        // I assume that you must save the state of work in a persistent way so that
        //   you can resume it if the job is cancelled. (SharePreferences, SQLite, etc.)

        for (i in 0..9) {
            Log.d(TAG, "$input - $i")

            if(isJobCancelled) {
                // The JobScheduler has cancelled the job, so this is a good time to save state
                //   for when the job is restarted. (I think?)
                // I am going to guess that you need to persist the state of the job every step
                //   of the way, so that you can resume it if the job is cancelled.
            }

            if (isStopped) return // if the system stops the job, stop the work.

            SystemClock.sleep(1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onStopCurrentWork(): Boolean { // if return `true`, the job will restart later with same intent.
        Log.d(TAG, "onStopCurrentWork, isJobCancelled=$isJobCancelled")

        isJobCancelled = true

        return super.onStopCurrentWork() // super method defaults to returning true
    }

    companion object {
        private const val TAG = "ExampleJobIntentService"
        fun enqueueWork(context: Context, work: Intent?) {
            enqueueWork(context, ExampleJobIntentService::class.java, 123, work!!)
        }
    }
}