package com.lillyjourney.ui.screens.tests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lillyjourney.data.db.TestEntity
import com.lillyjourney.data.repository.TestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TestsUiState(
    val tests: List<TestEntity> = emptyList(),
    val isLoading: Boolean = true,
    val showForm: Boolean = false,
)

@HiltViewModel
class TestsViewModel @Inject constructor(
    private val testRepository: TestRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(TestsUiState())
    val state: StateFlow<TestsUiState> = _state

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val all = testRepository.getByPregnancy("default")
            _state.value = _state.value.copy(tests = all, isLoading = false)
        }
    }

    fun showForm() { _state.value = _state.value.copy(showForm = true) }
    fun hideForm() { _state.value = _state.value.copy(showForm = false) }

    fun saveTest(name: String, date: String, result: String, unit: String, refRange: String, notes: String) {
        viewModelScope.launch {
            testRepository.save(
                TestEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    pregnancyId = "default",
                    name = name,
                    date = date,
                    result = result,
                    unit = unit,
                    referenceRange = refRange,
                    notes = notes,
                )
            )
            _state.value = _state.value.copy(showForm = false)
            load()
        }
    }

    fun deleteTest(id: String) {
        viewModelScope.launch { testRepository.delete(id); load() }
    }
}
