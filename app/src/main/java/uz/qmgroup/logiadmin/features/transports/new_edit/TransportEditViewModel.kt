package uz.qmgroup.logiadmin.features.transports.new_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.qmgroup.logiadmin.features.transports.datasource.TransportsDataSource
import uz.qmgroup.logiadmin.features.transports.models.Transport

class TransportEditViewModel(private val dataSource: TransportsDataSource): ViewModel() {
    private val _state = MutableStateFlow<TransportEditScreenState>(TransportEditScreenState.Default)
    val state = _state.asStateFlow()

    fun initialize() {
        _state.update { TransportEditScreenState.Default }
    }

    fun save(transport: Transport) {
        viewModelScope.launch {
            _state.update { TransportEditScreenState.SavePending }
            try {
                val newObject = dataSource.saveTransport(transport)

                _state.update { TransportEditScreenState.SaveCompleted(newObject) }
            } catch (e: Exception) {
                _state.update { TransportEditScreenState.SaveFailed(e.toString()) }
            }
        }
    }
}