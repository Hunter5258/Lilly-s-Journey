package com.lillyjourney.ui.screens.safety

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lillyjourney.data.db.SafetyAlertEntity
import com.lillyjourney.data.repository.SafetyAlertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SafetyUiState(
    val alerts: List<SafetyAlertEntity> = emptyList(),
    val isLoading: Boolean = true,
    val showForm: Boolean = false,
)

@HiltViewModel
class SafetyViewModel @Inject constructor(
    private val alertRepository: SafetyAlertRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SafetyUiState())
    val state: StateFlow<SafetyUiState> = _state

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            _state.value = _state.value.copy(
                alerts = alertRepository.getByPregnancy("default"),
                isLoading = false,
            )
        }
    }

    fun showForm() { _state.value = _state.value.copy(showForm = true) }
    fun hideForm() { _state.value = _state.value.copy(showForm = false) }

    fun saveAlert(warningType: String, severity: String, notes: String) {
        viewModelScope.launch {
            alertRepository.save(
                SafetyAlertEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    pregnancyId = "default",
                    warningType = warningType,
                    severity = severity,
                    timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    notes = notes,
                )
            )
            _state.value = _state.value.copy(showForm = false)
            load()
        }
    }

    fun acknowledge(id: String) {
        viewModelScope.launch { alertRepository.acknowledge(id); load() }
    }
}
