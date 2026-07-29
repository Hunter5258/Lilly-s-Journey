package com.lillyjourney.ui.screens.medicines

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lillyjourney.data.db.MedicineEntity
import com.lillyjourney.ui.components.LillyCard
import com.lillyjourney.ui.components.LillyEmptyState
import com.lillyjourney.ui.components.LillyFAB
import com.lillyjourney.ui.components.LillySearchBar
import com.lillyjourney.ui.theme.Danger
import com.lillyjourney.ui.theme.Primary
import com.lillyjourney.ui.theme.TextMuted
import com.lillyjourney.ui.theme.White
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun MedicinesScreen(
    viewModel: MedicinesViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val filtered = remember(state.medicines, state.searchQuery) { viewModel.filteredMedicines }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Medicines",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = "Track your prescribed medications",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 12.dp),
            )

            LillySearchBar(
                query = state.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                placeholder = "Search medicines...",
            )

            if (state.isLoading) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    androidx.compose.material3.CircularProgressIndicator(color = Primary)
                }
            } else if (filtered.isEmpty()) {
                LillyEmptyState(
                    icon = Icons.Filled.Medication,
                    title = if (state.searchQuery.isNotBlank()) "No matching medicines"
                            else "No medicines added yet",
                    message = if (state.searchQuery.isNotBlank()) "Try a different search term"
                            else "Tap + to add your first medication",
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filtered, key = { it.id }) { medicine ->
                        MedicineCard(
                            medicine = medicine,
                            onDelete = { viewModel.deleteMedicine(medicine.id) },
                        )
                    }
                    item { Spacer(Modifier.height(80.dp)) }
                }
            }
        }

        LillyFAB(
            onClick = viewModel::showForm,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        )
    }

    if (state.showForm) {
        MedicineFormDialog(
            onDismiss = viewModel::hideForm,
            onSave = { name, dosage, freq, days, start, notes ->
                viewModel.saveMedicine(name, dosage, freq, days, start, notes)
            },
        )
    }
}

@Composable
private fun MedicineCard(
    medicine: MedicineEntity,
    onDelete: () -> Unit,
) {
    val start = medicine.startDate?.let { LocalDate.parse(it) } ?: LocalDate.now()
    val end = medicine.endDate?.let { LocalDate.parse(it) }
    val totalDays = if (end != null) ChronoUnit.DAYS.between(start, end).toInt().coerceAtLeast(1) else 30
    val elapsedDays = ChronoUnit.DAYS.between(start, LocalDate.now()).toInt().coerceAtLeast(0)
    val progress = (elapsedDays.toFloat() / totalDays).coerceIn(0f, 1f)
    val isCompleted = progress >= 1f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 80f),
        label = "med_progress",
    )

    var showDeleteConfirm by remember { mutableStateOf(false) }

    LillyCard(
        title = {
            Text(
                text = medicine.name,
                style = MaterialTheme.typography.titleMedium,
            )
            if (medicine.dosage != null || medicine.frequency != null) {
                Text(
                    text = listOfNotNull(medicine.dosage, medicine.frequency).joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted,
                )
            }
        },
        badge = {
            Text(
                text = if (isCompleted) "Completed" else "Active",
                style = MaterialTheme.typography.labelSmall,
                color = if (isCompleted) MaterialTheme.colorScheme.outline else Primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    horizontal = 10.dp, vertical = 4.dp
                ).then(
                    Modifier
                        .let {
                            androidx.compose.foundation.background(
                                if (isCompleted) MaterialTheme.colorScheme.surfaceVariant
                                else Primary.copy(alpha = 0.12f),
                                RoundedCornerShape(12.dp)
                            )
                        }
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ),
            )
        },
    ) {
        Spacer(Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth().height(6.dp),
            color = Primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
        Spacer(Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Day $elapsedDays of $totalDays",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = Primary,
                fontWeight = FontWeight.Bold,
            )
        }
        if (medicine.notes.isNullOrBlank().not()) {
            Text(
                text = medicine.notes!!,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = Danger,
                )
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete ${medicine.name}?") },
            text = { Text("This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = { showDeleteConfirm = false; onDelete() }) {
                    Text("Delete", color = Danger)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            },
        )
    }
}

@Composable
private fun MedicineFormDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, dosage: String, freq: String, days: Int, start: String, notes: String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("Once daily") }
    var duration by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)) }
    var notes by remember { mutableStateOf("") }

    val frequencies = listOf("Once daily", "Twice daily", "Three times daily", "As needed")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Medicine", style = MaterialTheme.typography.headlineMedium) },
        text = {
            Column {
                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Medicine Name") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = dosage, onValueChange = { dosage = it },
                        label = { Text("Dosage") }, singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = fieldColors(),
                    )
                    OutlinedTextField(
                        value = duration, onValueChange = { duration = it },
                        label = { Text("Days") }, singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = fieldColors(),
                    )
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = startDate, onValueChange = { startDate = it },
                    label = { Text("Start Date") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes, onValueChange = { notes = it },
                    label = { Text("Notes") }, singleLine = false, maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(),
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(
                            name, dosage, frequency,
                            duration.toIntOrNull() ?: 0,
                            startDate, notes,
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(10.dp),
                enabled = name.isNotBlank(),
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
)
