package com.lillyjourney.ui.screens.reminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lillyjourney.data.db.ReminderEntity
import com.lillyjourney.data.repository.ReminderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class RemindersUiState(
    val reminders: List<ReminderEntity> = emptyList(),
    val isLoading: Boolean = true,
    val showForm: Boolean = false,
)

@HiltViewModel
class RemindersViewModel @Inject constructor(
    private val reminderRepository: ReminderRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(RemindersUiState())
    val state: StateFlow<RemindersUiState> = _state

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            _state.value = _state.value.copy(
                reminders = reminderRepository.getByPregnancy("default"),
                isLoading = false,
            )
        }
    }

    fun showForm() { _state.value = _state.value.copy(showForm = true) }
    fun hideForm() { _state.value = _state.value.copy(showForm = false) }

    fun saveReminder(type: String, scheduledTime: String, recurrence: String) {
        viewModelScope.launch {
            reminderRepository.save(
                ReminderEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    pregnancyId = "default",
                    type = type,
                    scheduledTime = scheduledTime,
                    recurrence = recurrence,
                )
            )
            _state.value = _state.value.copy(showForm = false)
            load()
        }
    }

    fun completeReminder(id: String) {
        viewModelScope.launch {
            reminderRepository.complete(id, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            load()
        }
    }

    fun deleteReminder(id: String) {
        viewModelScope.launch { reminderRepository.delete(id); load() }
    }
}
