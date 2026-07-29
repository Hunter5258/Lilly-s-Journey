package com.lillyjourney.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIntoContainer
import androidx.compose.animation.slideOutOfContainer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lillyjourney.ui.navigation.Screen
import com.lillyjourney.ui.screens.appointments.AppointmentsScreen
import com.lillyjourney.ui.screens.dashboard.DashboardScreen
import com.lillyjourney.ui.screens.medicines.MedicinesScreen
import com.lillyjourney.ui.screens.onboarding.OnboardingScreen
import com.lillyjourney.ui.screens.pregnancy.PregnancyScreen
import com.lillyjourney.ui.screens.prescriptions.PrescriptionsScreen
import com.lillyjourney.ui.screens.providers.ProvidersScreen
import com.lillyjourney.ui.screens.reminders.RemindersScreen
import com.lillyjourney.ui.screens.reports.ReportsScreen
import com.lillyjourney.ui.screens.safety.SafetyScreen
import com.lillyjourney.ui.screens.settings.SettingsScreen
import com.lillyjourney.ui.screens.symptoms.SymptomsScreen
import com.lillyjourney.ui.screens.tests.TestsScreen
import com.lillyjourney.ui.screens.vaccinations.VaccinationsScreen
import com.lillyjourney.ui.screens.vitals.VitalsScreen

@Composable
fun LillyNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route in listOf(
        Screen.Dashboard.route,
        Screen.Reminders.route,
        Screen.Providers.route,
        Screen.Safety.route,
        Screen.Settings.route,
    ) || currentDestination?.route?.startsWith("records/") == true

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                LillyBottomBar(
                    currentDestination = currentDestination,
                    onNavigate = { screen ->
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Onboarding.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                fadeIn(animationSpec = tween(220)) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(220)
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(140)) + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(140)
                )
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(220)) + slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(220)
                )
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(140)) + slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(140)
                )
            },
        ) {
            composable(Screen.Onboarding.route) { OnboardingScreen() }
            composable(Screen.Dashboard.route) { DashboardScreen() }
            composable(Screen.Pregnancy.route) { PregnancyScreen() }
            composable(Screen.Reminders.route) { RemindersScreen() }
            composable(Screen.Providers.route) { ProvidersScreen() }
            composable(Screen.Safety.route) { SafetyScreen() }
            composable(Screen.Reports.route) { ReportsScreen() }
            composable(Screen.Settings.route) { SettingsScreen() }
            composable(Screen.Medicines.route) { MedicinesScreen() }
            composable(Screen.Appointments.route) { AppointmentsScreen() }
            composable(Screen.Symptoms.route) { SymptomsScreen() }
            composable(Screen.Tests.route) { TestsScreen() }
            composable(Screen.Prescriptions.route) { PrescriptionsScreen() }
            composable(Screen.Vitals.route) { VitalsScreen() }
            composable(Screen.Vaccinations.route) { VaccinationsScreen() }
        }
    }
}
