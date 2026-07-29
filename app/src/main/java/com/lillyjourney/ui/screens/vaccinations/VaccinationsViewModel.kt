package com.lillyjourney.ui.screens.vaccinations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lillyjourney.data.db.VaccinationEntity
import com.lillyjourney.data.repository.VaccinationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VaccinationsUiState(
    val vaccinations: List<VaccinationEntity> = emptyList(),
    val isLoading: Boolean = true,
    val showForm: Boolean = false,
)

@HiltViewModel
class VaccinationsViewModel @Inject constructor(
    private val vaccinationRepository: VaccinationRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(VaccinationsUiState())
    val state: StateFlow<VaccinationsUiState> = _state

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val all = vaccinationRepository.getByPregnancy("default")
            _state.value = _state.value.copy(vaccinations = all, isLoading = false)
        }
    }

    fun showForm() { _state.value = _state.value.copy(showForm = true) }
    fun hideForm() { _state.value = _state.value.copy(showForm = false) }

    fun saveVaccination(name: String, dueDate: String, administeredDate: String, notes: String) {
        viewModelScope.launch {
            vaccinationRepository.save(
                VaccinationEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    pregnancyId = "default",
                    name = name,
                    dueDate = dueDate,
                    administeredDate = administeredDate,
                    status = if (administeredDate.isNotBlank()) "completed" else "pending",
                    notes = notes,
                )
            )
            _state.value = _state.value.copy(showForm = false)
            load()
        }
    }

    fun markCompleted(id: String) {
        viewModelScope.launch {
            vaccinationRepository.save(
                vaccinationRepository.getByPregnancy("default").first { it.id == id }.copy(
                    status = "completed",
                    administeredDate = java.time.LocalDate.now().toString(),
                )
            )
            load()
        }
    }

    fun deleteVaccination(id: String) {
        viewModelScope.launch { vaccinationRepository.delete(id); load() }
    }
}
