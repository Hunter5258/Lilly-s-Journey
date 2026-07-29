package com.lillyjourney.ui.screens.symptoms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lillyjourney.data.db.SymptomEntity
import com.lillyjourney.data.repository.SymptomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class SymptomsUiState(
    val symptoms: List<SymptomEntity> = emptyList(),
    val isLoading: Boolean = true,
    val showForm: Boolean = false,
    val selectedSeverity: Int = 3,
)

@HiltViewModel
class SymptomsViewModel @Inject constructor(
    private val symptomRepository: SymptomRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SymptomsUiState())
    val state: StateFlow<SymptomsUiState> = _state

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val all = symptomRepository.getByPregnancy("default")
            _state.value = _state.value.copy(symptoms = all, isLoading = false)
        }
    }

    fun showForm() { _state.value = _state.value.copy(showForm = true) }
    fun hideForm() { _state.value = _state.value.copy(showForm = false) }
    fun setSeverity(s: Int) { _state.value = _state.value.copy(selectedSeverity = s) }

    fun saveSymptom(name: String, notes: String) {
        viewModelScope.launch {
            symptomRepository.save(
                SymptomEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    pregnancyId = "default",
                    name = name,
                    severity = _state.value.selectedSeverity,
                    dateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    notes = notes,
                    isUrgent = _state.value.selectedSeverity >= 4,
                )
            )
            _state.value = _state.value.copy(showForm = false, selectedSeverity = 3)
            load()
        }
    }

    fun deleteSymptom(id: String) {
        viewModelScope.launch { symptomRepository.delete(id); load() }
    }
}
