package uz.qmgroup.logiadmin.features.shipments.new_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.qmgroup.logiadmin.features.shipments.datasource.ShipmentDataSource
import uz.qmgroup.logiadmin.features.shipments.models.Shipment

class ShipmentAddEditViewModel(private val dataSource: ShipmentDataSource) : ViewModel() {
    private val _state = MutableStateFlow<ShipmentEditScreenState>(ShipmentEditScreenState.Default)
    val state = _state.asStateFlow()

    fun initialize() {
        _state.update { ShipmentEditScreenState.Default }
    }

    fun save(shipment: Shipment) {
        viewModelScope.launch {
            _state.update { ShipmentEditScreenState.SavePending }
            try {
                dataSource.addNewShipment(shipment)

                _state.update { ShipmentEditScreenState.SaveCompleted }
            } catch (e: Exception) {
                _state.update { ShipmentEditScreenState.SaveFailed(e.toString()) }
            }
        }
    }
}