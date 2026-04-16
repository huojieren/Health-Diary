package com.huojieren.healthdiary.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.huojieren.healthdiary.database.HealthDatabase
import com.huojieren.healthdiary.model.RecordType

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Diet : Screen("diet", "饮食", Icons.Default.RestaurantMenu)
    data object Exercise : Screen("exercise", "运动", Icons.Default.FitnessCenter)
    data object Sleep : Screen("sleep", "睡眠", Icons.Default.Nightlight)
    data object Summary : Screen("summary", "汇总", Icons.Default.DateRange)
}

val bottomNavItems = listOf(Screen.Diet, Screen.Exercise, Screen.Sleep, Screen.Summary)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(database: HealthDatabase) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("健康日记") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Diet.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Diet.route) {
                RecordListScreen(database, RecordType.DIET)
            }
            composable(Screen.Exercise.route) {
                RecordListScreen(database, RecordType.EXERCISE)
            }
            composable(Screen.Sleep.route) {
                RecordListScreen(database, RecordType.SLEEP)
            }
            composable(Screen.Summary.route) {
                SummaryScreen(database)
            }
        }
    }
}