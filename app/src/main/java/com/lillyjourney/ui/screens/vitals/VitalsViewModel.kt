package com.lillyjourney.ui.screens.vitals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lillyjourney.data.db.VitalEntity
import com.lillyjourney.data.repository.VitalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VitalsUiState(
    val vitals: List<VitalEntity> = emptyList(),
    val isLoading: Boolean = true,
    val showForm: Boolean = false,
    val filter: String = "all",
)

@HiltViewModel
class VitalsViewModel @Inject constructor(
    private val vitalRepository: VitalRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(VitalsUiState())
    val state: StateFlow<VitalsUiState> = _state

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val all = vitalRepository.getByPregnancy("default")
            _state.value = _state.value.copy(vitals = all, isLoading = false)
        }
    }

    fun setFilter(f: String) { _state.value = _state.value.copy(filter = f) }
    fun showForm() { _state.value = _state.value.copy(showForm = true) }
    fun hideForm() { _state.value = _state.value.copy(showForm = false) }

    fun saveVital(type: String, value: Double?, systolic: Int?, diastolic: Int?, notes: String) {
        viewModelScope.launch {
            vitalRepository.save(
                VitalEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    pregnancyId = "default",
                    type = type,
                    value = value,
                    systolic = systolic,
                    diastolic = diastolic,
                    dateTime = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    notes = notes,
                )
            )
            _state.value = _state.value.copy(showForm = false)
            load()
        }
    }

    fun deleteVital(id: String) {
        viewModelScope.launch { vitalRepository.delete(id); load() }
    }

    val filteredVitals: List<VitalEntity>
        get() = if (_state.value.filter == "all") _state.value.vitals
                else _state.value.vitals.filter { it.type == _state.value.filter }
}
