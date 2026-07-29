package com.lillyjourney.ui.screens.pregnancy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PregnantWoman
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
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
import com.lillyjourney.ui.components.LillyEmptyState
import com.lillyjourney.ui.theme.Primary
import com.lillyjourney.ui.theme.TextMuted
import com.lillyjourney.ui.theme.White

@Composable
fun PregnancyScreen(
    viewModel: PregnancyViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Spacer(Modifier.height(8.dp))
        Text("Pregnancy", style = MaterialTheme.typography.headlineMedium)
        Text("Track your journey", style = MaterialTheme.typography.bodyMedium, color = TextMuted, modifier = Modifier.padding(bottom = 12.dp))

        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Primary)
            }
        } else if (state.pregnancy == null) {
            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.weight(1f))
                LillyEmptyState(
                    icon = Icons.Filled.PregnantWoman,
                    title = "No pregnancy data",
                    message = "Set up your pregnancy details",
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = viewModel::showForm,
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    shape = RoundedCornerShape(10.dp),
                ) { Text("Get Started") }
                Spacer(Modifier.weight(1f))
            }
        } else {
            val p = state.pregnancy!!
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Primary),
                elevation = CardDefaults.cardElevation(4.dp),
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Week 24", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, color = White)
                    Spacer(Modifier.height(4.dp))
                    Text("2nd Trimester", style = MaterialTheme.typography.bodyLarge, color = White.copy(alpha = 0.85f))
                    Spacer(Modifier.height(8.dp))
                    if (!p.dueDate.isNullOrBlank()) {
                        Text("Due ${p.dueDate}", style = MaterialTheme.typography.bodyMedium, color = White.copy(alpha = 0.7f))
                    }
                    if (!p.lmpDate.isNullOrBlank()) {
                        Text("LMP: ${p.lmpDate}", style = MaterialTheme.typography.bodySmall, color = White.copy(alpha = 0.6f))
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White),
                elevation = CardDefaults.cardElevation(1.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DetailRow("Status", p.status.replaceFirstChar { it.uppercase() })
                    DetailRow("Pregnancy #", p.pregnancyOrder.replaceFirstChar { it.uppercase() })
                    if (!p.dueDate.isNullOrBlank()) DetailRow("Due Date", p.dueDate!!)
                    if (!p.lmpDate.isNullOrBlank()) DetailRow("LMP", p.lmpDate!!)
                    if (!p.notes.isNullOrBlank()) DetailRow("Notes", p.notes!!)
                }
            }

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = viewModel::showForm,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(10.dp),
            ) { Text("Edit Pregnancy") }
        }
    }

    if (state.showForm) {
        PregnancyFormDialog(
            onDismiss = viewModel::hideForm,
            onSave = viewModel::savePregnancy,
            existing = state.pregnancy,
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextMuted, modifier = Modifier.weight(0.4f))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PregnancyFormDialog(
    onDismiss: () -> Unit,
    onSave: (lmpDate: String, dueDate: String, pregnancyOrder: String, notes: String) -> Unit,
    existing: PregnancyEntity?,
) {
    var lmpDate by remember { mutableStateOf(existing?.lmpDate ?: "") }
    var dueDate by remember { mutableStateOf(existing?.dueDate ?: "") }
    var pregnancyOrder by remember { mutableStateOf(existing?.pregnancyOrder ?: "first") }
    var notes by remember { mutableStateOf(existing?.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (existing != null) "Edit Pregnancy" else "New Pregnancy", style = MaterialTheme.typography.headlineMedium) },
        text = {
            Column {
                OutlinedTextField(
                    value = dueDate, onValueChange = { dueDate = it },
                    label = { Text("Due Date") }, singleLine = true,
                    placeholder = { Text("2026-08-15") },
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = lmpDate, onValueChange = { lmpDate = it },
                    label = { Text("LMP Date") }, singleLine = true,
                    placeholder = { Text("2025-11-08") },
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = pregnancyOrder, onValueChange = { pregnancyOrder = it },
                    label = { Text("Pregnancy #") }, singleLine = true,
                    placeholder = { Text("first, second, third") },
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
                onClick = { onSave(lmpDate, dueDate, pregnancyOrder, notes) },
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(10.dp),
                enabled = dueDate.isNotBlank() || lmpDate.isNotBlank(),
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
