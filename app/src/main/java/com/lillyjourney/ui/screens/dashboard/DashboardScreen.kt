package com.lillyjourney.ui.screens.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lillyjourney.ui.theme.BackgroundCard
import com.lillyjourney.ui.theme.Primary
import com.lillyjourney.ui.theme.PrimaryDark
import com.lillyjourney.ui.theme.Secondary
import com.lillyjourney.ui.theme.TextMuted
import com.lillyjourney.ui.theme.White

data class DashboardStat(val label: String, val count: Int, val emoji: String)

private val sampleStats = listOf(
    DashboardStat("Medicines", 3, "\uD83D\uDC8A"),
    DashboardStat("Appointments", 2, "\uD83D\uDCC5"),
    DashboardStat("Symptoms", 5, "\uD83D\uDE0A"),
    DashboardStat("Tests", 4, "\uD83D\uDD2C"),
    DashboardStat("Prescriptions", 1, "\uD83D\uDCDD"),
)

@Composable
fun DashboardScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item { WelcomeCard() }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                sampleStats.take(3).forEach { stat ->
                    StatCard(stat = stat, modifier = Modifier.weight(1f))
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                sampleStats.drop(3).forEach { stat ->
                    StatCard(stat = stat, modifier = Modifier.weight(1f))
                }
            }
        }
        item {
            Text(
                text = "Today's Medicines",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 8.dp),
            )
            PlaceholderCard("Prenatal Vitamin \u00B7 400mg", "Day 15 of 30")
        }
        item {
            Text(
                text = "Next Appointment",
                style = MaterialTheme.typography.titleLarge,
            )
            PlaceholderCard("Dr. Smith", "Fri, Mar 15 at 10:00 AM")
        }
        item {
            Text(
                text = "Recent Tests",
                style = MaterialTheme.typography.titleLarge,
            )
            PlaceholderCard("Complete Blood Count", "12.5 g/dL")
        }
        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun WelcomeCard() {
    var rotationX by remember { mutableFloatStateOf(0f) }
    var rotationY by remember { mutableFloatStateOf(0f) }

    val animatedRotX by animateFloatAsState(
        targetValue = rotationX,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f),
    )
    val animatedRotY by animateFloatAsState(
        targetValue = rotationY,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f),
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                rotationX = animatedRotX
                rotationY = animatedRotY
                cameraDistance = 12f * density
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, _, _ ->
                    rotationY = (pan.x / size.width) * 6f
                    rotationX = -(pan.y / size.height) * 6f
                }
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Primary,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {
            Column {
                Text(
                    text = "Hello, there!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = White,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Week 24 \u00B7 2nd Trimester",
                    style = MaterialTheme.typography.bodyLarge,
                    color = White.copy(alpha = 0.85f),
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Due August 15, 2026",
                    style = MaterialTheme.typography.bodyMedium,
                    color = White.copy(alpha = 0.7f),
                )
            }
        }
    }
}

@Composable
private fun StatCard(stat: DashboardStat, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stat.emoji, fontSize = 24.sp)
            Text(
                text = "${stat.count}",
                style = MaterialTheme.typography.headlineMedium,
                color = Primary,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stat.label,
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
            )
        }
    }
}

@Composable
private fun PlaceholderCard(title: String, subtitle: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
            )
        }
    }
}
