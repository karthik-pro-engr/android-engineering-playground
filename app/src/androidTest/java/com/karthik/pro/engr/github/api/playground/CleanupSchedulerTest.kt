package com.karthik.pro.engr.github.api.playground

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.common.truth.Truth.assertThat
import com.karthik.pro.engr.github.api.playground.worker.CLEANUP_WORK_NAME
import com.karthik.pro.engr.github.api.playground.worker.CleanupScheduler
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalCoroutinesApi::class)
class CleanupSchedulerTest {

    private lateinit var workManager: WorkManager

    private lateinit var cleanupScheduler: CleanupScheduler

    @Before
    fun setup() {

        workManager = mockk(relaxed = true)

        cleanupScheduler = CleanupScheduler(
            workManager = workManager
        )
    }

    @Test
    fun schedulePeriodicCleanup_creates_24_hour_periodic_work_request() {

        val requestSlot =
            slot<PeriodicWorkRequest>()

        every {
            workManager.enqueueUniquePeriodicWork(
                any(),
                any(),
                capture(requestSlot)
            )
        } returns mockk()

        cleanupScheduler.schedulePeriodicCleanup()

        assertThat(
            requestSlot.captured.workSpec.intervalDuration
        ).isEqualTo(
            TimeUnit.HOURS.toMillis(24)
        )

        verify(exactly = 1) {
            workManager.enqueueUniquePeriodicWork(
                CLEANUP_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                any()
            )
        }
    }

    @Test
    fun schedulePeriodicCleanup_enqueues_unique_periodic_work() {

        // Act
        cleanupScheduler.schedulePeriodicCleanup()

        // Assert
        verify(exactly = 1) {
            workManager.enqueueUniquePeriodicWork(
                CLEANUP_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                any()
            )
        }
    }
}