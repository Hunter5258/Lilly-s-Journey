package com.lillyjourney.ui.screens.providers

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalHospital
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
import com.lillyjourney.data.db.ProviderEntity
import com.lillyjourney.ui.components.LillyCard
import com.lillyjourney.ui.components.LillyEmptyState
import com.lillyjourney.ui.components.LillyFAB
import com.lillyjourney.ui.theme.Danger
import com.lillyjourney.ui.theme.Primary
import com.lillyjourney.ui.theme.TextMuted

@Composable
fun ProvidersScreen(
    viewModel: ProvidersViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(8.dp))
            Text("Providers", style = MaterialTheme.typography.headlineMedium)
            Text("Your care team", style = MaterialTheme.typography.bodyMedium, color = TextMuted, modifier = Modifier.padding(bottom = 12.dp))

            if (state.isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Primary)
                }
            } else if (state.providers.isEmpty()) {
                LillyEmptyState(
                    icon = Icons.Filled.LocalHospital,
                    title = "No providers",
                    message = "Add your doctors & specialists",
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.providers, key = { it.id }) { provider ->
                        ProviderCard(
                            provider = provider,
                            onDelete = { viewModel.deleteProvider(provider.id) },
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
        ProviderFormDialog(
            onDismiss = viewModel::hideForm,
            onSave = viewModel::saveProvider,
        )
    }
}

@Composable
private fun ProviderCard(provider: ProviderEntity, onDelete: () -> Unit) {
    var showDelete by remember { mutableStateOf(false) }

    LillyCard(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).let {
                        androidx.compose.foundation.background(
                            Primary.copy(alpha = 0.15f),
                            RoundedCornerShape(10.dp),
                        ).padding(8.dp)
                    },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(provider.name.take(2).uppercase(), fontWeight = FontWeight.Bold, color = Primary)
                }
                Spacer(Modifier.size(8.dp))
                Column {
                    Text(provider.name, style = MaterialTheme.typography.titleMedium)
                    if (!provider.specialty.isNullOrBlank()) {
                        Text(provider.specialty!!, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }
                }
            }
        },
    ) {
        if (!provider.clinic.isNullOrBlank() || !provider.phone.isNullOrBlank()) {
            Column {
                if (!provider.clinic.isNullOrBlank()) Text(provider.clinic!!, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                if (!provider.phone.isNullOrBlank()) Text(provider.phone!!, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
        }
        if (!provider.address.isNullOrBlank()) {
            Text(provider.address!!, style = MaterialTheme.typography.bodySmall, color = TextMuted, modifier = Modifier.padding(top = 2.dp))
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
            title = { Text("Delete provider?") },
            confirmButton = { TextButton(onClick = { showDelete = false; onDelete() }) { Text("Delete", color = Danger) } },
            dismissButton = { TextButton(onClick = { showDelete = false }) { Text("Cancel") } },
        )
    }
}

@Composable
private fun ProviderFormDialog(
    onDismiss: () -> Unit,
    onSave: (name: String, specialty: String, clinic: String, phone: String, address: String, notes: String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("") }
    var clinic by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Provider", style = MaterialTheme.typography.headlineMedium) },
        text = {
            Column {
                OutlinedTextField(
                    value = name, onValueChange = { name = it },
                    label = { Text("Name") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = specialty, onValueChange = { specialty = it },
                    label = { Text("Specialty") }, singleLine = true,
                    placeholder = { Text("e.g. OB/GYN, Midwife") },
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = clinic, onValueChange = { clinic = it },
                    label = { Text("Clinic / Hospital") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone, onValueChange = { phone = it },
                    label = { Text("Phone") }, singleLine = true,
                    modifier = Modifier.fillMaxWidth(), colors = fieldColors(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = address, onValueChange = { address = it },
                    label = { Text("Address") }, singleLine = true,
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
                onClick = { if (name.isNotBlank()) onSave(name, specialty, clinic, phone, address, notes) },
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
