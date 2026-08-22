package com.iroha71.koigoi_client.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.auth0.android.result.Credentials
import com.iroha71.koigoi_client.components.AppLayout
import com.iroha71.koigoi_client.views.Home
import com.iroha71.koigoi_client.views.Landing

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    isLoading: Boolean,
    credentials: Credentials?,
    onLogin: (String?) -> Unit,
    onLogout: () -> Unit,
) {
    LaunchedEffect(credentials) {
        if (credentials != null) {
            navController.navigate(Home) {
                popUpTo(Landing) { inclusive = true }
            }
        } else {
            navController.navigate(Landing) {
                popUpTo(Home) { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = Landing, modifier = modifier) {
        composable<Landing> {
            Landing(isLoading = isLoading, onLogin = onLogin)
        }
        composable<Home> {
            AppLayout(title = "Home") {
                Home(credentials = credentials, onLogout = onLogout)
            }
        }
    }
}
