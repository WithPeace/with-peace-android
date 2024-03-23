package com.app.profileeditor.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.app.profileeditor.ProfileEditorRoute

const val PROFILE_EDITOR_ROUTE = "profileEditorRoute"

fun NavController.navigateProfileEditor(navOptions: NavOptions? = null) {
    navigate(PROFILE_EDITOR_ROUTE, navOptions)
}

fun NavGraphBuilder.profileEditorNavGraph(
    onShowSnackBar: (message: String) -> Unit,
) {
    composable(route = PROFILE_EDITOR_ROUTE) {
        ProfileEditorRoute(
            onShowSnackBar = onShowSnackBar,
        )
    }
}