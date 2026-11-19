package com.example.studentorganizer

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun StudentOrganizerApp(settings: AppSettings) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Shared sample-data state (hoisted here so every screen sees the same list)
    val assignments = remember { mutableStateListOf<Assignment>().apply { addAll(sampleAssignments) } }
    val nextId = remember { intArrayOf(sampleAssignments.size + 1) }

    val toggleComplete: (Int) -> Unit = { id ->
        val index = assignments.indexOfFirst { it.id == id }
        if (index >= 0) {
            assignments[index] = assignments[index].copy(isCompleted = !assignments[index].isCompleted)
        }
    }
    val goToAdd: () -> Unit = { navController.navigate(Screen.AddAssignment.route) }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (currentRoute != Screen.AddAssignment.route) {
                BottomBar(navController = navController, currentRoute = currentRoute)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    studentName = sampleStudentName,
                    courses = sampleCourses,
                    assignments = assignments,
                    onToggleComplete = toggleComplete,
                    onAddClick = goToAdd
                )
            }
            composable(Screen.Assignments.route) {
                AssignmentsScreen(
                    assignments = assignments,
                    onToggleComplete = toggleComplete,
                    onAddClick = goToAdd
                )
            }
            composable(Screen.Profile.route) { ProfileScreen() }
            composable(Screen.Settings.route) { SettingsScreen(settings) }
            composable(Screen.AddAssignment.route) {
                AddAssignmentScreen(
                    onSave = { newAssignment ->
                        assignments.add(newAssignment.copy(id = nextId[0]++))
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}
