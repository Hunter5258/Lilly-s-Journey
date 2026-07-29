package com.lillyjourney.ui.screens.symptoms

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Sick
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lillyjourney.data.db.SymptomEntity
import com.lillyjourney.ui.components.LillyCard
import com.lillyjourney.ui.components.LillyEmptyState
import com.lillyjourney.ui.components.LillyFAB
import com.lillyjourney.ui.theme.Danger
import com.lillyjourney.ui.theme.Primary
import com.lillyjourney.ui.theme.TextMuted
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun SymptomsScreen(
    viewModel: SymptomsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            Text("Symptoms", style = MaterialTheme.typography.headlineMedium)
            Text("Track how you're feeling", style = MaterialTheme.typography.bodyMedium, color = TextMuted, modifier = Modifier.padding(bottom = 12.dp))

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else if (state.symptoms.isEmpty()) {
                LillyEmptyState(
                    icon = Icons.Filled.Sick,
                    title = "No symptoms logged",
                    message = "Add a symptom to start tracking",
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.symptoms, key = { it.id }) { symptom ->
                        SymptomCard(
                            symptom = symptom,
                            onDelete = { viewModel.deleteSymptom(symptom.id) },
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
        SymptomFormDialog(
            selectedSeverity = state.selectedSeverity,
            onSeverityChange = viewModel::setSeverity,
            onDismiss = viewModel::hideForm,
            onSave = viewModel::saveSymptom,
        )
    }
}

@Composable
private fun SymptomCard(symptom: SymptomEntity, onDelete: () -> Unit) {
    var showDelete by remember { mutableStateOf(false) }
    val dt = symptom.dateTime?.let {
        try { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) } catch (_: Exception) { null }
    }

    LillyCard(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(symptom.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                SeverityDots(symptom.severity)
            }
        },
        subtitle = dt?.format(DateTimeFormatter.ofPattern("MMM d, h:mm a")),
    ) {
        if (!symptom.notes.isNullOrBlank()) {
            Text(symptom.notes!!, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = { showDelete = true }) {
                Icon(Icons.Filled.Delete, "Delete", tint = Danger)
            }
        }
    }

    if (showDelete) {
        AlertDialog(
            onDismissRequest = { showDelete = false },
            title = { Text("Delete symptom?") },
            confirmButton = { TextButton(onClick = { showDelete = false; onDelete() }) { Text("Delete", color = Danger) } },
            dismissButton = { TextButton(onClick = { showDelete = false }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun SeverityDots(severity: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        repeat(5) { i ->
            Box(
                Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .let {
                        if (i < severity) it.let {
                            androidx.compose.foundation.background(
                                when {
                                    severity <= 2 -> Primary.copy(alpha = 0.5f)
                                    severity <= 3 -> Primary
                                    else -> Danger
                                }
                            )
                        } else it.let {
                            androidx.compose.foundation.background(
                                MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
            )
        }
    }
}

@Composable
private fun SymptomFormDialog(
    selectedSeverity: Int,
    onSeverityChange: (Int) -> Unit,
    onDismiss: () -> Unit,
    onSave: (name: String, notes: String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Log Symptom", style = MaterialTheme.typography.headlineMedium) },
        text = {
            Column {
                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Symptom") }, singleLine = true,
                    placeholder = { Text("e.g. Headache, Nausea") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                )
                Spacer(Modifier.height(12.dp))
                Text("Severity", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    (1..5).forEach { s ->
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .let {
                                    if (s <= selectedSeverity)
                                        it.let {
                                            androidx.compose.foundation.background(
                                                when { s <= 2 -> Primary.copy(alpha = 0.5f); s <= 3 -> Primary; else -> Danger }
                                            )
                                        }
                                    else it.let {
                                        androidx.compose.foundation.background(
                                            MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    }
                                }
                                .let {
                                    it.let {
                                        androidx.compose.foundation.clickable { onSeverityChange(s) }
                                    }
                                },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text("$s", color = if (s <= selectedSeverity) {
                                androidx.compose.ui.graphics.Color.White
                            } else {
                                TextMuted
                            }, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = notes, onValueChange = { notes = it },
                    label = { Text("Notes") }, maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (name.isNotBlank()) onSave(name, notes) },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(10.dp),
                enabled = name.isNotBlank(),
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
