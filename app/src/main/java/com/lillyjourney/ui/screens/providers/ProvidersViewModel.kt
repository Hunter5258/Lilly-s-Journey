package com.lillyjourney.ui.screens.providers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lillyjourney.data.db.ProviderEntity
import com.lillyjourney.data.repository.ProviderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProvidersUiState(
    val providers: List<ProviderEntity> = emptyList(),
    val isLoading: Boolean = true,
    val showForm: Boolean = false,
)

@HiltViewModel
class ProvidersViewModel @Inject constructor(
    private val providerRepository: ProviderRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ProvidersUiState())
    val state: StateFlow<ProvidersUiState> = _state

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            _state.value = _state.value.copy(
                providers = providerRepository.getAll(),
                isLoading = false,
            )
        }
    }

    fun showForm() { _state.value = _state.value.copy(showForm = true) }
    fun hideForm() { _state.value = _state.value.copy(showForm = false) }

    fun saveProvider(name: String, specialty: String, clinic: String, phone: String, address: String, notes: String) {
        viewModelScope.launch {
            providerRepository.save(
                ProviderEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    name = name,
                    specialty = specialty,
                    clinic = clinic,
                    phone = phone,
                    address = address,
                    notes = notes,
                )
            )
            _state.value = _state.value.copy(showForm = false)
            load()
        }
    }

    fun deleteProvider(id: String) {
        viewModelScope.launch { providerRepository.delete(id); load() }
    }
}
