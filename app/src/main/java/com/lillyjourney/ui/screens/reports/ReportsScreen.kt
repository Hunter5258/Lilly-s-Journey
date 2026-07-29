package com.lillyjourney.ui.screens.reports

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lillyjourney.ui.theme.Primary
import com.lillyjourney.ui.theme.TextMuted
import com.lillyjourney.ui.theme.White

data class ReportCard(val title: String, val value: String, val subtitle: String)

private val sampleReports = listOf(
    ReportCard("Medicines", "3 Active", "2 completed courses"),
    ReportCard("Appointments", "2 Upcoming", "4 attended"),
    ReportCard("Symptoms", "12 Logged", "Most common: Nausea"),
    ReportCard("Tests", "5 Results", "2 flagged for review"),
    ReportCard("Weight", "68.5 kg", "Gained 8.5 kg total"),
)

@Composable
fun ReportsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(Modifier.height(8.dp))
        Text("Reports", style = MaterialTheme.typography.headlineMedium)
        Text("Your pregnancy summary", style = MaterialTheme.typography.bodyMedium, color = TextMuted, modifier = Modifier.padding(bottom = 12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Primary),
            elevation = CardDefaults.cardElevation(4.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Pregnancy Summary", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = White)
                Spacer(Modifier.height(4.dp))
                Text("Week 24 · 2nd Trimester", style = MaterialTheme.typography.bodyLarge, color = White.copy(alpha = 0.85f))
                Text("Due August 15, 2026", style = MaterialTheme.typography.bodyMedium, color = White.copy(alpha = 0.7f))
            }
        }

        Spacer(Modifier.height(16.dp))
        sampleReports.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                row.forEach { report ->
                    ReportCardView(report = report, modifier = Modifier.weight(1f))
                }
                if (row.size == 1) Spacer(Modifier.weight(1f))
            }
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun ReportCardView(report: ReportCard, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(1.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(report.value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Primary, textAlign = TextAlign.Center)
            Spacer(Modifier.height(2.dp))
            Text(report.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
            Text(report.subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted, textAlign = TextAlign.Center)
        }
    }
}
