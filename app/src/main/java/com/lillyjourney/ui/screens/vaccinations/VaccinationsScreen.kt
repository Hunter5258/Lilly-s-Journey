package com.lillyjourney.ui.screens.vaccinations

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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Vaccines
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lillyjourney.data.db.VaccinationEntity
import com.lillyjourney.ui.components.LillyCard
import com.lillyjourney.ui.components.LillyEmptyState
import com.lillyjourney.ui.components.LillyFAB
import com.lillyjourney.ui.theme.Danger
import com.lillyjourney.ui.theme.Primary
import com.lillyjourney.ui.theme.Success
import com.lillyjourney.ui.theme.TextMuted

@Composable
fun VaccinationsScreen(
    viewModel: VaccinationsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            Text("Vaccinations", style = MaterialTheme.typography.headlineMedium)
            Text("Immunizations & due dates", style = MaterialTheme.typography.bodyMedium, color = TextMuted, modifier = Modifier.padding(bottom = 12.dp))

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else if (state.vaccinations.isEmpty()) {
                LillyEmptyState(
                    icon = Icons.Filled.Vaccines,
                    title = "No vaccinations",
                    message = "Track your immunizations",
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.vaccinations, key = { it.id }) { vac ->
                        VaccinationCard(
                            vaccination = vac,
                            onComplete = { viewModel.markCompleted(vac.id) },
                            onDelete = { viewModel.deleteVaccination(vac.id) },
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
        VaccinationFormDialog(
            onDismiss = viewModel::hideForm,
            onSave = viewModel::saveVaccination,
        )
    }
}

@Composable
private fun VaccinationCard(
    vaccination: VaccinationEntity,
    onComplete: () -> Unit,
    onDelete: () -> Unit,
) {
    var showDelete by remember { mutableStateOf(false) }
    val isDone = vaccination.status == "completed"

    LillyCard(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(vaccination.name, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Icon(
                    if (isDone) Icons.Filled.CheckCircle else Icons.Filled.Vaccines,
                    contentDescription = null,
                    tint = if (isDone) Success else Primary,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        },
        subtitle = if (isDone) "Completed ${vaccination.administeredDate}" else "Due ${vaccination.dueDate ?: "—"}",
    ) {
        if (!vaccination.notes.isNullOrBlank()) {
            Text(vaccination.notes!!, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (!isDone) {
                TextButton(onClick = onComplete) {
                    Text("Mark Done", color = Success)
                }
            }
            IconButton(onClick = { showDelete = true }) {
                Icon(Icons.Filled.Delete, "Delete", tint = Danger)
            }
        }
    }

    if (showDelete) {
        AlertDialog(
            onDismissRequest = { showDelete = false },
            title = { Text("Delete vaccination?") },
            confirmButton = { TextButton(onClick = { showDelete = false; onDelete() }) { Text("Delete", color = Danger) } },
            dismissButton = { TextButton(onClick = { showDelete = false }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun VaccinationFormDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, dueDate: String, administeredDate: String, notes: String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var administeredDate by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Vaccination", style = MaterialTheme.typography.headlineMedium) },
        text = {
            Column {
                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Vaccine Name") }, singleLine = true,
                    placeholder = { Text("e.g. Tdap, Flu Shot") },
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = dueDate, onValueChange = { dueDate = it },
                    label = { Text("Due Date") }, singleLine = true,
                    placeholder = { Text("2026-08-15") },
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = administeredDate, onValueChange = { administeredDate = it },
                    label = { Text("Date Given (optional)") }, singleLine = true,
                    placeholder = { Text("2026-07-01") },
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
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
                onClick = { if (name.isNotBlank()) onSave(name, dueDate, administeredDate, notes) },
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
