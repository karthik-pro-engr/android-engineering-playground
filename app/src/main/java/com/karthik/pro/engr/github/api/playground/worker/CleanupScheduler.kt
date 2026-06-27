package com.karthik.pro.engr.github.api.playground.worker

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CleanupScheduler @Inject constructor(
    private val workManager: WorkManager
) {

    fun schedulePeriodicCleanup() {

        val request =
            PeriodicWorkRequestBuilder<CleanupWorker>(
                24,
                TimeUnit.HOURS
            )
                .build()

        workManager.enqueueUniquePeriodicWork(
            CLEANUP_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}