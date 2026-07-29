package com.lillyjourney.ui.screens.prescriptions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lillyjourney.data.db.PrescriptionEntity
import com.lillyjourney.data.repository.PrescriptionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PrescriptionsUiState(
    val prescriptions: List<PrescriptionEntity> = emptyList(),
    val isLoading: Boolean = true,
    val showForm: Boolean = false,
)

@HiltViewModel
class PrescriptionsViewModel @Inject constructor(
    private val prescriptionRepository: PrescriptionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PrescriptionsUiState())
    val state: StateFlow<PrescriptionsUiState> = _state

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val all = prescriptionRepository.getByPregnancy("default")
            _state.value = _state.value.copy(prescriptions = all, isLoading = false)
        }
    }

    fun showForm() { _state.value = _state.value.copy(showForm = true) }
    fun hideForm() { _state.value = _state.value.copy(showForm = false) }

    fun savePrescription(title: String, doctor: String, date: String, notes: String) {
        viewModelScope.launch {
            prescriptionRepository.save(
                PrescriptionEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    pregnancyId = "default",
                    date = date,
                    title = title,
                    notes = notes,
                )
            )
            _state.value = _state.value.copy(showForm = false)
            load()
        }
    }

    fun deletePrescription(id: String) {
        viewModelScope.launch { prescriptionRepository.delete(id); load() }
    }
}
