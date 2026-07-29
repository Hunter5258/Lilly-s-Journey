package com.lillyjourney.ui.screens.pregnancy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lillyjourney.data.db.PregnancyEntity
import com.lillyjourney.data.repository.PregnancyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PregnancyUiState(
    val pregnancy: PregnancyEntity? = null,
    val isLoading: Boolean = true,
    val showForm: Boolean = false,
)

@HiltViewModel
class PregnancyViewModel @Inject constructor(
    private val pregnancyRepository: PregnancyRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(PregnancyUiState())
    val state: StateFlow<PregnancyUiState> = _state

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            _state.value = _state.value.copy(
                pregnancy = pregnancyRepository.getActive(),
                isLoading = false,
            )
        }
    }

    fun showForm() { _state.value = _state.value.copy(showForm = true) }
    fun hideForm() { _state.value = _state.value.copy(showForm = false) }

    fun savePregnancy(lmpDate: String, dueDate: String, pregnancyOrder: String, notes: String) {
        viewModelScope.launch {
            pregnancyRepository.save(
                PregnancyEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    lmpDate = lmpDate,
                    dueDate = dueDate,
                    pregnancyOrder = pregnancyOrder,
                    notes = notes,
                )
            )
            _state.value = _state.value.copy(showForm = false)
            load()
        }
    }
}
