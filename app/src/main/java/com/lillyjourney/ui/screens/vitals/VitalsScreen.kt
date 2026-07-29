package com.lillyjourney.ui.screens.vitals

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.lillyjourney.data.db.VitalEntity
import com.lillyjourney.ui.components.LillyCard
import com.lillyjourney.ui.components.LillyEmptyState
import com.lillyjourney.ui.components.LillyFAB
import com.lillyjourney.ui.theme.Danger
import com.lillyjourney.ui.theme.Primary
import com.lillyjourney.ui.theme.TextMuted

private val vitalTypes = listOf("all", "weight", "blood_pressure", "heart_rate", "temperature", "blood_sugar")

@Composable
fun VitalsScreen(
    viewModel: VitalsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val filtered = remember(state.vitals, state.filter) { viewModel.filteredVitals }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            Text("Vitals", style = MaterialTheme.typography.headlineMedium)
            Text("Weight, BP, heart rate & more", style = MaterialTheme.typography.bodyMedium, color = TextMuted, modifier = Modifier.padding(bottom = 12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                vitalTypes.forEach { t ->
                    OutlinedButton(
                        onClick = { viewModel.setFilter(t) },
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, if (state.filter == t) Primary else MaterialTheme.colorScheme.outlineVariant,
                        ),
                    ) {
                        Text(
                            t.replace("_", " ").replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (state.filter == t) Primary else TextMuted,
                        )
                    }
                }
            }
            Spacer(Modifier.height(8.dp))

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else if (filtered.isEmpty()) {
                LillyEmptyState(
                    icon = Icons.Filled.MonitorHeart,
                    title = "No vitals recorded",
                    message = "Track your weight, blood pressure & more",
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filtered, key = { it.id }) { vital ->
                        VitalCard(
                            vital = vital,
                            onDelete = { viewModel.deleteVital(vital.id) },
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
        VitalFormDialog(
            onDismiss = viewModel::hideForm,
            onSave = viewModel::saveVital,
        )
    }
}

@Composable
private fun VitalCard(vital: VitalEntity, onDelete: () -> Unit) {
    var showDelete by remember { mutableStateOf(false) }
    val displayValue = when (vital.type) {
        "blood_pressure" -> "${vital.systolic}/${vital.diastolic} mmHg"
        "weight" -> "${vital.value?.toInt() ?: "—"} kg"
        "heart_rate" -> "${vital.value?.toInt() ?: "—"} bpm"
        "temperature" -> "${vital.value ?: "—"} °C"
        "blood_sugar" -> "${vital.value ?: "—"} mmol/L"
        else -> vital.value?.toString() ?: "—"
    }

    LillyCard(
        title = {
            Text(
                vital.type.replace("_", " ").replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium,
            )
        },
        subtitle = vital.dateTime,
    ) {
        Text(displayValue, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Primary)
        if (!vital.notes.isNullOrBlank()) {
            Text(vital.notes!!, style = MaterialTheme.typography.bodySmall, color = TextMuted)
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
            title = { Text("Delete vital?") },
            confirmButton = { TextButton(onClick = { showDelete = false; onDelete() }) { Text("Delete", color = Danger) } },
            dismissButton = { TextButton(onClick = { showDelete = false }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun VitalFormDialog(
    onDismiss: () -> Unit,
    onSave: (type: String, value: Double?, systolic: Int?, diastolic: Int?, notes: String) -> Unit,
) {
    var selectedType by remember { mutableStateOf("") }
    var value by remember { mutableStateOf("") }
    var systolic by remember { mutableStateOf("") }
    var diastolic by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Vital", style = MaterialTheme.typography.headlineMedium) },
        text = {
            Column {
                Box {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(if (selectedType.isBlank()) "Select type" else selectedType.replace("_", " ").replaceFirstChar { it.uppercase() })
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        vitalTypes.filter { it != "all" }.forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t.replace("_", " ").replaceFirstChar { it.uppercase() }) },
                                onClick = { selectedType = t; expanded = false },
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                if (selectedType == "blood_pressure") {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = systolic, onValueChange = { systolic = it },
                            label = { Text("Systolic") }, singleLine = true,
                            modifier = Modifier.weight(1f), colors = fieldColors(),
                        )
                        OutlinedTextField(
                            value = diastolic, onValueChange = { diastolic = it },
                            label = { Text("Diastolic") }, singleLine = true,
                            modifier = Modifier.weight(1f), colors = fieldColors(),
                        )
                    }
                } else if (selectedType.isNotBlank()) {
                    OutlinedTextField(
                        value = value, onValueChange = { value = it },
                        label = { Text("Value") }, singleLine = true,
                        modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                    )
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
                onClick = {
                    onSave(
                        selectedType,
                        value.toDoubleOrNull(),
                        systolic.toIntOrNull(),
                        diastolic.toIntOrNull(),
                        notes,
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(10.dp),
                enabled = selectedType.isNotBlank(),
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
