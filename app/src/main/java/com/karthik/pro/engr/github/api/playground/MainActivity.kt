package com.karthik.pro.engr.github.api.playground

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.karthik.pro.engr.feedback.api.ui.screens.FeedbackFab
import com.karthik.pro.engr.feedback.api.ui.screens.FeedbackStateText
import com.karthik.pro.engr.feedback.api.ui.viewmodel.FeedbackEvent
import com.karthik.pro.engr.feedback.api.ui.viewmodel.FeedbackUiEffect
import com.karthik.pro.engr.github.api.playground.app.feedback.AppFeedbackController
import com.karthik.pro.engr.github.api.playground.presentation.common.theme.GithubapiplaygroundTheme
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailRoute
import com.karthik.pro.engr.github.api.playground.presentation.repos.RepoListRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var feedbackController: AppFeedbackController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GithubapiplaygroundTheme {
                SetContent(appFeedbackController = feedbackController)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetContent(
    modifier: Modifier = Modifier,
    appFeedbackController: AppFeedbackController
) {
    val current = LocalContext.current
    val feedbackUiState by appFeedbackController.uiState.collectAsState()
    Log.d("MainActivity", "SetContent: $feedbackUiState")
    if (BuildConfig.ENABLE_APP_DISTRIBUTION) {
        LaunchedEffect(Unit) {
            appFeedbackController.uiEffect.collectLatest { effect ->
                when (effect) {
                    FeedbackUiEffect.LaunchEmail -> {
                        appFeedbackController.feedbackActions.launchFeedbackEmail(
                            current,
                            "karthik.pro.engr@gmail.com"
                        )
                    }
                }

            }
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind { drawRect(Color(0x2200FF00)) },
            topBar = {
                TopAppBar(
                    title = { Text("Github Repositories") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            bottomBar = {
                FeedbackStateText(modifier = Modifier.fillMaxWidth(), feedbackUiState)
            },
            floatingActionButton = {
                FeedbackFab(
                    modifier,
                    { messageResId ->
                        appFeedbackController.onEvent(
                            FeedbackEvent.SendFeedback(
                                messageResId
                            )
                        )
                    })
            }
        ) { innerPadding ->
            RepoListRoute(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    } else {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind { drawRect(Color(0x2200FF00)) },
            topBar = {
                TopAppBar(
                    title = { Text("Github Repositories") },
                    colors = TopAppBarDefaults.mediumTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { innerPadding ->
            RepoDetailRoute(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}