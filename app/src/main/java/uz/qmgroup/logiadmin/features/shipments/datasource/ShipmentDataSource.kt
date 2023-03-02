package uz.qmgroup.logiadmin.features.shipments.datasource

import kotlinx.coroutines.flow.Flow
import uz.qmgroup.logiadmin.features.shipments.models.Shipment
import uz.qmgroup.logiadmin.features.shipments.models.ShipmentStatus
import uz.qmgroup.logiadmin.features.transports.models.Transport

interface ShipmentDataSource {
    fun getShipments(query: String, statuses: List<ShipmentStatus>? = null): Flow<List<Shipment>>

    suspend fun addNewShipment(shipment: Shipment)

    suspend fun setStatus(shipment: Shipment, status: ShipmentStatus)

    suspend fun assignTransport(shipment: Shipment, transport: Transport)

    suspend fun updateShipmentStatusToOnWay(shipment: Shipment, receiverCompanyName: String)
}