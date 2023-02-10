package uz.qmgroup.logiadmin.features.transports.new_edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.qmgroup.logiadmin.features.transports.datasource.TransportsDataSource
import uz.qmgroup.logiadmin.features.transports.models.Transport

class TransportEditViewModel(val dataSource: TransportsDataSource): ViewModel() {
    private val _state = MutableStateFlow<TransportEditScreenState>(TransportEditScreenState.Default)
    val state = _state.asStateFlow()

    fun initialize() {
        _state.update { TransportEditScreenState.Default }
    }

    fun save(transport: Transport) {
        viewModelScope.launch {
            _state.update { TransportEditScreenState.SavePending }
            try {
//                dataSource.addNewShipment(shipment)
                delay(3000)
                Log.d("LogiAdmin", "save: Tranport $transport")

                _state.update { TransportEditScreenState.SaveCompleted }
            } catch (e: Exception) {
                _state.update { TransportEditScreenState.SaveFailed(e.toString()) }
            }
        }
    }
}