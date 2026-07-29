package com.lillyjourney.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sick
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import com.lillyjourney.ui.navigation.Screen

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
)

private val primaryItems = listOf(
    BottomNavItem(Screen.Dashboard, Icons.Filled.Home),
    BottomNavItem(Screen.Reminders, Icons.Filled.Notifications),
    BottomNavItem(Screen.Providers, Icons.Filled.LocalHospital),
    BottomNavItem(Screen.Safety, Icons.Filled.Warning),
    BottomNavItem(Screen.Settings, Icons.Filled.Settings),
)

private val recordsItems = listOf(
    BottomNavItem(Screen.Medicines, Icons.Filled.Medication),
    BottomNavItem(Screen.Prescriptions, Icons.Filled.ReceiptLong),
    BottomNavItem(Screen.Tests, Icons.Filled.Science),
    BottomNavItem(Screen.Vitals, Icons.Filled.MonitorHeart),
    BottomNavItem(Screen.Vaccinations, Icons.Filled.Vaccines),
    BottomNavItem(Screen.Appointments, Icons.Filled.CalendarMonth),
    BottomNavItem(Screen.Symptoms, Icons.Filled.Sick),
)

@Composable
fun LillyBottomBar(
    currentDestination: NavDestination?,
    onNavigate: (Screen) -> Unit,
) {
    var recordsExpanded by remember { mutableStateOf(false) }

    Column {
        // Records sub-nav (expandable)
        AnimatedVisibility(
            visible = recordsExpanded,
            enter = slideInVertically(animationSpec = spring()) { it } + fadeIn(),
            exit = slideOutVertically(animationSpec = spring()) { it } + fadeOut(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                recordsItems.forEach { item ->
                    val selected = currentDestination?.route == item.screen.route
                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onNavigate(item.screen) }
                            .padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.screen.label,
                            tint = if (selected) MaterialTheme.colorScheme.primary
                                   else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp),
                        )
                        Text(
                            text = item.screen.label,
                            fontSize = 9.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                            color = if (selected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        // Main bottom nav
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
        ) {
            primaryItems.forEach { item ->
                val selected = currentDestination?.route == item.screen.route
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        if (item.screen.route == Screen.Providers.route) {
                            recordsExpanded = !recordsExpanded
                        } else {
                            recordsExpanded = false
                            onNavigate(item.screen)
                        }
                    },
                    icon = {
                        Icon(imageVector = item.icon, contentDescription = item.screen.label)
                    },
                    label = { Text(text = item.screen.label, fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                )
            }
        }
    }
}
