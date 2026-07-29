package com.lillyjourney.ui.screens.tests

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
import androidx.compose.material.icons.filled.Science
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
import com.lillyjourney.data.db.TestEntity
import com.lillyjourney.ui.components.LillyCard
import com.lillyjourney.ui.components.LillyEmptyState
import com.lillyjourney.ui.components.LillyFAB
import com.lillyjourney.ui.theme.Danger
import com.lillyjourney.ui.theme.Primary
import com.lillyjourney.ui.theme.TextMuted

@Composable
fun TestsScreen(
    viewModel: TestsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            Text("Tests", style = MaterialTheme.typography.headlineMedium)
            Text("Lab results & screenings", style = MaterialTheme.typography.bodyMedium, color = TextMuted, modifier = Modifier.padding(bottom = 12.dp))

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else if (state.tests.isEmpty()) {
                LillyEmptyState(
                    icon = Icons.Filled.Science,
                    title = "No tests recorded",
                    message = "Add your first lab result",
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.tests, key = { it.id }) { test ->
                        TestCard(
                            test = test,
                            onDelete = { viewModel.deleteTest(test.id) },
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
        TestFormDialog(
            onDismiss = viewModel::hideForm,
            onSave = viewModel::saveTest,
        )
    }
}

@Composable
private fun TestCard(test: TestEntity, onDelete: () -> Unit) {
    var showDelete by remember { mutableStateOf(false) }

    LillyCard(
        title = { Text(test.name, style = MaterialTheme.typography.titleMedium) },
        subtitle = test.date,
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                if (!test.result.isNullOrBlank()) {
                    Text(
                        test.result + (test.unit?.let { " $it" } ?: ""),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Primary,
                    )
                }
                if (!test.referenceRange.isNullOrBlank()) {
                    Text("Ref: ${test.referenceRange}", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }
            }
            if (!test.notes.isNullOrBlank()) {
                Text(test.notes!!, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
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
            title = { Text("Delete test?") },
            confirmButton = { TextButton(onClick = { showDelete = false; onDelete() }) { Text("Delete", color = Danger) } },
            dismissButton = { TextButton(onClick = { showDelete = false }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun TestFormDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, date: String, result: String, unit: String, refRange: String, notes: String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var refRange by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Test", style = MaterialTheme.typography.headlineMedium) },
        text = {
            Column {
                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Test Name") }, singleLine = true,
                    placeholder = { Text("e.g. Complete Blood Count") },
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = date, onValueChange = { date = it },
                    label = { Text("Date") }, singleLine = true,
                    placeholder = { Text("2026-07-15") },
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = result, onValueChange = { result = it },
                        label = { Text("Result") }, singleLine = true,
                        modifier = Modifier.weight(1f), colors = fieldColors(),
                    )
                    OutlinedTextField(
                        value = unit, onValueChange = { unit = it },
                        label = { Text("Unit") }, singleLine = true,
                        modifier = Modifier.weight(1f), colors = fieldColors(),
                    )
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = refRange, onValueChange = { refRange = it },
                    label = { Text("Reference Range") }, singleLine = true,
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
                onClick = { if (name.isNotBlank()) onSave(name, date, result, unit, refRange, notes) },
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
