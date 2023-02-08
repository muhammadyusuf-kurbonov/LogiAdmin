package uz.qmgroup.logiadmin.features.shipments

import uz.qmgroup.logiadmin.features.shipments.models.Shipment

sealed class ShipmentScreenState {
    object Loading: ShipmentScreenState()

    data class DataFetched(val list: List<Shipment>): ShipmentScreenState()

    object NoData: ShipmentScreenState()
}