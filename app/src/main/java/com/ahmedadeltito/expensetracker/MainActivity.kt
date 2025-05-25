package com.ahmedadeltito.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.ahmedadeltito.expensetracker.presentation.navigation.AppDestination
import com.ahmedadeltito.expensetracker.presentation.navigation.AppNavHost
import com.ahmedadeltito.expensetracker.presentation.navigation.AppNavigator
import com.ahmedadeltito.expensetracker.ui.theme.ExpenseTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseTrackerTheme {
                val navController = rememberNavController()
                val appNavigator = remember(navController) {
                    AppNavigator(navController)
                }
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier.fillMaxSize().padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavHost(
                            appNavigator = appNavigator,
                            snackbarHostState = snackbarHostState,
                            startDestination = AppDestination.ExpenseList.destination
                        )
                    }
                }
            }
        }
    }
}