package com.lillyjourney.ui.screens.safety

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lillyjourney.ui.components.LillyCard
import com.lillyjourney.ui.components.LillyEmptyState
import com.lillyjourney.ui.components.LillyFAB
import com.lillyjourney.ui.theme.Danger
import com.lillyjourney.ui.theme.Primary
import com.lillyjourney.ui.theme.TextMuted
import com.lillyjourney.ui.theme.Warning

@Composable
fun SafetyScreen(
    viewModel: SafetyViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            Text("Safety", style = MaterialTheme.typography.headlineMedium)
            Text("Alerts & warning signs", style = MaterialTheme.typography.bodyMedium, color = TextMuted, modifier = Modifier.padding(bottom = 12.dp))

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else if (state.alerts.isEmpty()) {
                LillyEmptyState(
                    icon = Icons.Filled.Warning,
                    title = "No safety alerts",
                    message = "Log any warning signs or concerns",
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.alerts, key = { it.id }) { alert ->
                        SafetyAlertCard(
                            alert = alert,
                            onAcknowledge = { viewModel.acknowledge(alert.id) },
                        )
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }

        LillyFAB(
            onClick = viewModel::showForm,
            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp),
        )
    }

    if (state.showForm) {
        SafetyFormDialog(
            onDismiss = viewModel::hideForm,
            onSave = viewModel::saveAlert,
        )
    }
}

@Composable
private fun SafetyAlertCard(
    alert: SafetyAlertEntity,
    onAcknowledge: () -> Unit,
) {
    val severityColor = when (alert.severity) {
        "high" -> Danger; "medium" -> Warning; else -> TextMuted
    }

    LillyCard(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(alert.warningType.replace("_", " ").replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.let {
                        androidx.compose.foundation.background(
                            severityColor.copy(alpha = 0.2f),
                            RoundedCornerShape(8.dp),
                        ).padding(horizontal = 8.dp, vertical = 2.dp)
                    },
                ) {
                    Text(alert.severity.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = severityColor)
                }
            }
        },
        subtitle = alert.timestamp,
    ) {
        if (!alert.notes.isNullOrBlank()) {
            Text(alert.notes!!, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
        if (!alert.acknowledged) {
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onAcknowledge) {
                Text("Acknowledge", color = Primary)
            }
        }
    }
}

@Composable
private fun SafetyFormDialog(
    onDismiss: () -> Unit,
    onSave: (warningType: String, severity: String, notes: String) -> Unit,
) {
    var warningType by remember { mutableStateOf("") }
    var severity by remember { mutableStateOf("high") }
    var notes by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Alert", style = MaterialTheme.typography.headlineMedium) },
        text = {
            Column {
                OutlinedTextField(
                    value = warningType, onValueChange = { warningType = it },
                    label = { Text("Warning Type") }, singleLine = true,
                    placeholder = { Text("e.g. bleeding, headache") },
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
                Spacer(Modifier.height(12.dp))
                Box {
                    OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("Severity: ${severity.uppercase()}")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        listOf("high", "medium", "low").forEach { s ->
                            DropdownMenuItem(text = { Text(s.replaceFirstChar { it.uppercase() }) }, onClick = { severity = s; expanded = false })
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes, onValueChange = { notes = it },
                    label = { Text("Notes") }, maxLines = 3,
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (warningType.isNotBlank()) onSave(warningType, severity, notes) },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(10.dp),
                enabled = warningType.isNotBlank(),
            ) { Text("Save") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
)
