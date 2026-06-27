package com.karthik.pro.engr.github.api.playground

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ListenableWorker
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkerParameters
import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.domain.usecase.CleanupInactiveDataUseCase
import com.karthik.pro.engr.github.api.playground.worker.CLEANUP_WORK_NAME
import com.karthik.pro.engr.github.api.playground.worker.CleanupWorker
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class CleanupWorkerTest {

    private lateinit var context: Context
    private lateinit var workerParams: WorkerParameters
    private lateinit var cleanupInactiveDataUseCase: CleanupInactiveDataUseCase

    private lateinit var worker: CleanupWorker

    @Before
    fun setup() {

        context = ApplicationProvider.getApplicationContext()

        workerParams = mockk(relaxed = true)

        cleanupInactiveDataUseCase = mockk()

        worker = CleanupWorker(
            appContext = context,
            workerParams = workerParams,
            cleanupInactiveDataUseCase = cleanupInactiveDataUseCase
        )
    }

    @Test
    fun doWork_returns_success_when_cleanup_completes() = runTest {

        // Arrange
        coEvery {
            cleanupInactiveDataUseCase()
        } just Runs

        // Act
        val result = worker.doWork()

        // Assert
        assertThat(result)
            .isEqualTo(ListenableWorker.Result.success())

        coVerify(exactly = 1) {
            cleanupInactiveDataUseCase()
        }
    }

    @Test
    fun doWork_returns_retry_when_cleanup_throws_exception() = runTest {

        // Arrange
        coEvery {
            cleanupInactiveDataUseCase()
        } throws RuntimeException("DB Failure")

        // Act
        val result = worker.doWork()

        // Assert
        assertThat(result)
            .isEqualTo(ListenableWorker.Result.retry())

        coVerify(exactly = 1) {
            cleanupInactiveDataUseCase()
        }
    }


}