package uz.qmgroup.logiadmin.features.shipments.datasource

import kotlinx.coroutines.flow.Flow
import uz.qmgroup.logiadmin.features.shipments.models.Shipment
import uz.qmgroup.logiadmin.features.transports.models.Transport

interface ShipmentDataSource {
    fun getShipments(query: String): Flow<List<Shipment>>

    suspend fun addNewShipment(shipment: Shipment)

    suspend fun cancelShipment(shipment: Shipment)

    suspend fun assignTransport(shipment: Shipment, transport: Transport)
}