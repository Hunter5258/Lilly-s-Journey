package com.lillyjourney.ui.screens.appointments

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import com.lillyjourney.data.db.AppointmentEntity
import com.lillyjourney.ui.components.LillyCard
import com.lillyjourney.ui.components.LillyEmptyState
import com.lillyjourney.ui.components.LillyFAB
import com.lillyjourney.ui.theme.Danger
import com.lillyjourney.ui.theme.Primary
import com.lillyjourney.ui.theme.TextMuted
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AppointmentsScreen(
    viewModel: AppointmentsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val filtered = remember(state.appointments, state.filter) { viewModel.filteredAppointments }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            Text("Appointments", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Doctor visits & checkups",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                modifier = Modifier.padding(bottom = 12.dp),
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = state.filter == "upcoming",
                    onClick = { viewModel.setFilter("upcoming") },
                    label = { Text("Upcoming") },
                    colors = chipColors(),
                )
                FilterChip(
                    selected = state.filter == "past",
                    onClick = { viewModel.setFilter("past") },
                    label = { Text("Past") },
                    colors = chipColors(),
                )
            }
            Spacer(Modifier.height(8.dp))

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    androidx.compose.material3.CircularProgressIndicator(color = Primary)
                }
            } else if (filtered.isEmpty()) {
                LillyEmptyState(
                    icon = Icons.Filled.CalendarMonth,
                    title = "No ${state.filter} appointments",
                    message = if (state.filter == "upcoming") "Schedule your next checkup"
                            else "Your past visits will appear here",
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filtered, key = { it.id }) { appt ->
                        AppointmentCard(
                            appointment = appt,
                            isUpcoming = state.filter == "upcoming",
                            onAttended = { viewModel.markAttended(appt.id) },
                            onMissed = { viewModel.markMissed(appt.id) },
                            onDelete = { viewModel.deleteAppointment(appt.id) },
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
        AppointmentFormDialog(
            onDismiss = viewModel::hideForm,
            onSave = viewModel::saveAppointment,
        )
    }
}

@Composable
private fun AppointmentCard(
    appointment: AppointmentEntity,
    isUpcoming: Boolean,
    onAttended: () -> Unit,
    onMissed: () -> Unit,
    onDelete: () -> Unit,
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val dt = appointment.dateTime?.let {
        try { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) } catch (_: Exception) { null }
    }

    LillyCard(
        title = {
            Text(appointment.purpose ?: "Appointment", style = MaterialTheme.typography.titleMedium)
        },
        subtitle = dt?.format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy  h:mm a")) ?: appointment.dateTime,
        badge = {
            Text(
                text = if (isUpcoming) "Upcoming" else "Past",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (isUpcoming) Primary else MaterialTheme.colorScheme.outline,
                modifier = Modifier.let {
                    androidx.compose.foundation.background(
                        if (isUpcoming) Primary.copy(alpha = 0.12f)
                        else MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(12.dp)
                    ).padding(horizontal = 8.dp, vertical = 2.dp)
                },
            )
        },
    ) {
        if (!appointment.location.isNullOrBlank()) {
            Text(
                appointment.location,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
            )
        }
        if (!appointment.notes.isNullOrBlank()) {
            Text(
                appointment.notes!!,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(onClick = { showDeleteConfirm = true }) {
                Icon(Icons.Filled.Delete, "Delete", tint = Danger)
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete appointment?") },
            text = { Text("This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = { showDeleteConfirm = false; onDelete() }) {
                    Text("Delete", color = Danger)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) { Text("Cancel") }
            },
        )
    }
}

@Composable
private fun AppointmentFormDialog(
    onDismiss: () -> Unit,
    onSave: (doctorName: String, dateTime: String, location: String, notes: String) -> Unit,
) {
    var doctor by remember { mutableStateOf("") }
    var dateTime by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Appointment", style = MaterialTheme.typography.headlineMedium) },
        text = {
            Column {
                OutlinedTextField(
                    value = doctor, onValueChange = { doctor = it },
                    label = { Text("Doctor Name") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = dateTime, onValueChange = { dateTime = it },
                    label = { Text("Date & Time") }, singleLine = true,
                    placeholder = { Text("2026-08-15T10:00") },
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = location, onValueChange = { location = it },
                    label = { Text("Location") }, singleLine = true,
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
                onClick = {
                    if (doctor.isNotBlank() && dateTime.isNotBlank()) {
                        onSave(doctor, dateTime, location, notes)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(10.dp),
                enabled = doctor.isNotBlank() && dateTime.isNotBlank(),
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@Composable
private fun chipColors() = FilterChipDefaults.filterChipColors(
    selectedContainerColor = Primary.copy(alpha = 0.15f),
    selectedLabelColor = Primary,
)

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
)
