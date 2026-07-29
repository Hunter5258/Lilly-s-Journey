package com.lillyjourney.ui.screens.appointments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lillyjourney.data.db.AppointmentEntity
import com.lillyjourney.data.repository.AppointmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class AppointmentsUiState(
    val appointments: List<AppointmentEntity> = emptyList(),
    val filter: String = "upcoming",
    val isLoading: Boolean = true,
    val showForm: Boolean = false,
)

@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val appointmentRepository: AppointmentRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AppointmentsUiState())
    val state: StateFlow<AppointmentsUiState> = _state

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val all = appointmentRepository.getUpcoming()
            _state.value = _state.value.copy(
                appointments = all,
                isLoading = false,
            )
        }
    }

    fun setFilter(filter: String) { _state.value = _state.value.copy(filter = filter) }
    fun showForm() { _state.value = _state.value.copy(showForm = true) }
    fun hideForm() { _state.value = _state.value.copy(showForm = false) }

    fun saveAppointment(doctorName: String, dateTime: String, location: String, notes: String) {
        viewModelScope.launch {
            appointmentRepository.save(
                AppointmentEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    purpose = doctorName,
                    dateTime = dateTime,
                    location = location,
                    notes = notes,
                    status = "upcoming",
                )
            )
            _state.value = _state.value.copy(showForm = false)
            load()
        }
    }

    fun markAttended(id: String) {
        viewModelScope.launch { appointmentRepository.markAttended(id); load() }
    }

    fun markMissed(id: String) {
        viewModelScope.launch { appointmentRepository.markMissed(id); load() }
    }

    fun deleteAppointment(id: String) {
        viewModelScope.launch { appointmentRepository.delete(id); load() }
    }

    val filteredAppointments: List<AppointmentEntity>
        get() {
            val now = LocalDateTime.now()
            return if (_state.value.filter == "upcoming") {
                _state.value.appointments.filter { a ->
                    a.dateTime?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)?.isAfter(now) } ?: false
                }.sortedBy { it.dateTime }
            } else {
                _state.value.appointments.filter { a ->
                    a.dateTime?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME)?.isBefore(now) } ?: false
                }.sortedByDescending { it.dateTime }
            }
        }
}
