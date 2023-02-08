package uz.qmgroup.logiadmin.features.shipments.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.qmgroup.logiadmin.features.shipments.models.Shipment
import uz.qmgroup.logiadmin.features.shipments.models.ShipmentStatus
import uz.qmgroup.logiadmin.features.shipments.models.Transport
import uz.qmgroup.logiadmin.features.shipments.models.TransportType

class TestShipmentDataSource: ShipmentDataSource {
    override fun getShipments(query: String): Flow<List<Shipment>> = flow {
        emit(
            (0 until  15).map {
                Shipment(
                    orderId = it.toLong(),
                    note = "Test shipment",
                    orderPrefix = "CC",
                    transportId = null,
                    status = ShipmentStatus.ASSIGNED,
                    pickoffPlace = "Tashkent",
                    destinationPlace = "Chiroqchi",
                    price = 5000000.0,
                    author = "Diyorbek",
                    transport = Transport(
                        transportId = 0,
                        driverName = "Marat aka",
                        driverPhone = "+998913975538",
                        transportNumber = "40L544KA",
                        type = TransportType.REFRIGERATOR_MODE
                    ),
                    company = "Umid OOO",
                    databaseId = ""
                )
            }
        )
    }

    override suspend fun addNewShipment(shipment: Shipment) {
        TODO("Not yet implemented")
    }
}