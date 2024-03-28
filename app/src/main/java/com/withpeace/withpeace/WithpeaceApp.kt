package com.withpeace.withpeace

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme
import com.withpeace.withpeace.navigation.WithpeaceNavHost
import kotlinx.coroutines.launch

@Composable
fun WithpeaceApp(
    startDestination: String,
    navController: NavHostController = rememberNavController(),
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    fun showSnackBar(message: String) = coroutineScope.launch {
        snackBarHostState.showSnackbar(message)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val parentDestination = currentDestination?.parent

    Scaffold(
        bottomBar = {
            if (
                BottomTab.contains(parentDestination?.route ?: currentDestination?.route ?: "")
            ) {
                MainBottomBar(
                    modifier = Modifier.height(56.dp),
                    currentDestination = if (parentDestination?.route == null) {
                        currentDestination ?: return@Scaffold
                    } else parentDestination,
                    navController = navController,
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = WithpeaceTheme.colors.SystemWhite,
    ) { innerPadding ->
        WithpeaceNavHost(
            modifier = Modifier.padding(innerPadding),
            onShowSnackBar = ::showSnackBar,
            startDestination = startDestination,
            navController = navController,
        )
    }
}
