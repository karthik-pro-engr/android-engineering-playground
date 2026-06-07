package com.karthik.pro.engr.github.api.playground.presentation.navigation

object AppDestinations {
    const val REPO_LIST_ROUTE = "repo_list"
    const val REPO_DETAIL_ROUTE = "repo_detail"
    const val REPO_NAME_ARG = "repo_name"
    const val REPO_OWNER_ARG = "owner"
    const val REPO_DETAIL_FULL_ROUTE = "$REPO_DETAIL_ROUTE/{$REPO_OWNER_ARG}/{$REPO_NAME_ARG}"

    const val DEEP_LINK_SCHEME =
        "githubapi"

    const val DEEP_LINK_HOST =
        "karthik.dev"

    const val REPO_DETAIL_DEEP_LINK =
        "$DEEP_LINK_SCHEME://$DEEP_LINK_HOST/{$REPO_OWNER_ARG}/{$REPO_NAME_ARG}"
}