package com.karthik.pro.engr.github.api.playground.app

import android.app.Application
import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.karthik.pro.engr.github.api.playground.BuildConfig
import com.karthik.pro.engr.github.api.playground.worker.CleanupScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class GithubPlaygroundApp : Application(),
    Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var cleanupScheduler: CleanupScheduler

    override val workManagerConfiguration: Configuration
        get() {
            return Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
        }

    override fun onCreate() {
        super.onCreate()
        cleanupScheduler.schedulePeriodicCleanup()
        if (BuildConfig.ENABLE_APP_DISTRIBUTION) {
            setupBetaFeatures()
        }

    }

    private fun setupBetaFeatures() {
        val app = FirebaseApp.initializeApp(this)
        Log.d("BetaApplication", "FirebaseApp.initializeApp returned: ${app?.name ?: "null"}")
        // Also list installed Firebase apps for debugging
        val apps = FirebaseApp.getApps(this)
        Log.d("BetaApplication", "FirebaseApp.getApps(): ${apps.map { it.name }}")
        /*   FeedbackBinder.provideAppFeedbackController = { activity: ComponentActivity ->
               val firebaseFeedbackSender = FirebaseFeedbackSender()
               val feedbackViewModelFactory = FeedbackViewModelFactory(firebaseFeedbackSender)
               val vm =
                   ViewModelProvider(
                       activity,
                       feedbackViewModelFactory
                   ).get(FeedbackViewModel::class.java)
               BetaAppFeedbackController(vm, BetaAppFeedbackActions(vm))
           }*/
    }
}