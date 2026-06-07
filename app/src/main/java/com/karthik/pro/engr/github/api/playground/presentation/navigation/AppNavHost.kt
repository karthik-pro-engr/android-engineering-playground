package com.karthik.pro.engr.github.api.playground.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.karthik.pro.engr.github.api.playground.presentation.navigation.AppDestinations.REPO_DETAIL_DEEP_LINK
import com.karthik.pro.engr.github.api.playground.presentation.navigation.AppDestinations.REPO_DETAIL_FULL_ROUTE
import com.karthik.pro.engr.github.api.playground.presentation.navigation.AppDestinations.REPO_NAME_ARG
import com.karthik.pro.engr.github.api.playground.presentation.navigation.AppDestinations.REPO_LIST_ROUTE
import com.karthik.pro.engr.github.api.playground.presentation.navigation.AppDestinations.REPO_OWNER_ARG
import com.karthik.pro.engr.github.api.playground.presentation.repo.RepoDetailRoute
import com.karthik.pro.engr.github.api.playground.presentation.repos.RepoListRoute

@Composable
fun AppNavHost(modifier: Modifier = Modifier, navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = REPO_LIST_ROUTE
    ) {
        composable(route = REPO_LIST_ROUTE) {
            RepoListRoute(onRepoClick = { owner, repoName ->
                navController.navigate("${AppDestinations.REPO_DETAIL_ROUTE}/$owner/$repoName")
            })
        }

        composable(
            route = REPO_DETAIL_FULL_ROUTE, // repo_detail/karthik/github-api-playground
            arguments = listOf(
                navArgument(REPO_OWNER_ARG) {
                    type = NavType.StringType
                },
                navArgument(REPO_NAME_ARG) {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(navDeepLink { // githubapi://karthik.dev/karthik-pro-engr/admin-tools
                uriPattern = REPO_DETAIL_DEEP_LINK
            })
        ) {
            RepoDetailRoute(modifier = modifier) {
                navController.navigateUp()
            }
        }
    }


}