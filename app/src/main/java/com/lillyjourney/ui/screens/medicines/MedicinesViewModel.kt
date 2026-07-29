package com.lillyjourney.ui.screens.medicines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lillyjourney.data.db.MedicineEntity
import com.lillyjourney.data.repository.MedicineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class MedicineUiState(
    val medicines: List<MedicineEntity> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = true,
    val showForm: Boolean = false,
)

@HiltViewModel
class MedicinesViewModel @Inject constructor(
    private val medicineRepository: MedicineRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(MedicineUiState())
    val state: StateFlow<MedicineUiState> = _state

    init { loadMedicines() }

    fun loadMedicines() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val meds = medicineRepository.getActive()
            _state.value = _state.value.copy(
                medicines = meds,
                isLoading = false,
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }

    fun showForm() { _state.value = _state.value.copy(showForm = true) }
    fun hideForm() { _state.value = _state.value.copy(showForm = false) }

    fun saveMedicine(
        name: String,
        dosage: String,
        frequency: String,
        durationDays: Int,
        startDate: String,
        notes: String,
    ) {
        viewModelScope.launch {
            val id = java.util.UUID.randomUUID().toString()
            medicineRepository.save(
                MedicineEntity(
                    id = id,
                    name = name,
                    dosage = dosage,
                    frequency = frequency,
                    startDate = startDate,
                    endDate = if (durationDays > 0) {
                        LocalDate.parse(startDate).plusDays(durationDays.toLong())
                            .format(DateTimeFormatter.ISO_LOCAL_DATE)
                    } else null,
                    notes = notes,
                    status = "active",
                )
            )
            _state.value = _state.value.copy(showForm = false)
            loadMedicines()
        }
    }

    fun deleteMedicine(id: String) {
        viewModelScope.launch {
            medicineRepository.delete(id)
            loadMedicines()
        }
    }

    val filteredMedicines: List<MedicineEntity>
        get() {
            val q = _state.value.searchQuery.lowercase()
            return if (q.isBlank()) _state.value.medicines
            else _state.value.medicines.filter {
                it.name.lowercase().contains(q) ||
                it.dosage?.lowercase()?.contains(q) == true
            }
        }
}
