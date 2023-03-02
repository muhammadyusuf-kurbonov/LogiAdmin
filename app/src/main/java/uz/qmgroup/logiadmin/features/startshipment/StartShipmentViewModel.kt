package uz.qmgroup.logiadmin.features.startshipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.qmgroup.logiadmin.features.shipments.datasource.ShipmentDataSource
import uz.qmgroup.logiadmin.features.shipments.models.Shipment

class StartShipmentViewModel(
    private val dataSource: ShipmentDataSource
): ViewModel() {
    private val _state = MutableStateFlow<StartShipmentDialogState>(StartShipmentDialogState.AwaitingInput)
    val state = _state.asStateFlow()

    fun updateStatusToSent(shipment: Shipment, companyName: String) {
        viewModelScope.launch {
            _state.update { StartShipmentDialogState.Saving }

            dataSource.updateShipmentStatusToOnWay(
                shipment,
                companyName
            )

            _state.update { StartShipmentDialogState.Updated }
        }
    }
}