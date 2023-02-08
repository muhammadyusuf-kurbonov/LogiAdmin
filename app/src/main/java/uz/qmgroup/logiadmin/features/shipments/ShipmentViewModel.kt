package uz.qmgroup.logiadmin.features.shipments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uz.qmgroup.logiadmin.features.shipments.datasource.ShipmentDataSource
import uz.qmgroup.logiadmin.features.shipments.models.Shipment

class ShipmentViewModel(private val dataSource: ShipmentDataSource) : ViewModel() {
    private val _state = MutableStateFlow<ShipmentScreenState>(ShipmentScreenState.Loading)
    val state = _state.asStateFlow()

    private var currentQueryListener: Job? = null

    fun search(query: String) {
        currentQueryListener?.cancel()

        currentQueryListener = viewModelScope.launch {
            _state.emit(ShipmentScreenState.Loading)

            _state.emitAll(dataSource.getShipments(query).map { shipments ->
                if (shipments.isEmpty())
                    ShipmentScreenState.NoData
                else
                    ShipmentScreenState.DataFetched(shipments)
            })
        }
    }

    fun cancel(shipment: Shipment) {
        viewModelScope.launch { dataSource.cancelShipment(shipment) }
    }
}