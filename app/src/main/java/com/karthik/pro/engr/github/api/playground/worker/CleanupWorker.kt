package com.karthik.pro.engr.github.api.playground.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.karthik.pro.engr.github.api.domain.usecase.CleanupInactiveDataUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class CleanupWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cleanupInactiveDataUseCase: CleanupInactiveDataUseCase
) : CoroutineWorker(
    appContext,
    workerParams
) {

    override suspend fun doWork(): Result {



        return try {

            cleanupInactiveDataUseCase()

            Result.success()

        } catch (exception: Exception) {


            Result.retry()
        }
    }
}