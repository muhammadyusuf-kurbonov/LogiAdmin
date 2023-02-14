package uz.qmgroup.logiadmin.features.shipments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uz.qmgroup.logiadmin.features.shipmentfilter.ShipmentFilter
import uz.qmgroup.logiadmin.features.shipments.datasource.ShipmentDataSource
import uz.qmgroup.logiadmin.features.shipments.models.Shipment
import uz.qmgroup.logiadmin.features.shipments.models.ShipmentStatus
import uz.qmgroup.logiadmin.features.transports.models.Transport

class ShipmentViewModel(private val dataSource: ShipmentDataSource) : ViewModel() {
    private val _state = MutableStateFlow<ShipmentScreenState>(ShipmentScreenState.Loading)
    val state = _state.asStateFlow()

    private var currentQueryListener: Job? = null

    fun search(query: String, filter: ShipmentFilter? = null) {
        currentQueryListener?.cancel()

        currentQueryListener = viewModelScope.launch {
            _state.emit(ShipmentScreenState.Loading)

            _state.emitAll(dataSource.getShipments(query, filter?.status).map { shipments ->
                if (shipments.isEmpty())
                    ShipmentScreenState.NoData
                else
                    ShipmentScreenState.DataFetched(shipments.sortedBy {
                        it.status
                    })
            })
        }
    }

    fun cancel(shipment: Shipment) {
        viewModelScope.launch { dataSource.setStatus(shipment, ShipmentStatus.CANCELLED) }
    }

    fun assignDriver(shipment: Shipment, transport: Transport) {
        viewModelScope.launch {
            dataSource.assignTransport(shipment, transport)
        }
    }

    fun startShipment(shipment: Shipment) {
        viewModelScope.launch { dataSource.setStatus(shipment, ShipmentStatus.ON_WAY) }
    }

    fun completeShipment(shipment: Shipment) {
        viewModelScope.launch { dataSource.setStatus(shipment, ShipmentStatus.COMPLETED) }
    }
}