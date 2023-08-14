package uz.qmgroup.logiadmin.features.shipments

import uz.qmgroup.logiadmin.features.shipments.models.Shipment

sealed class ShipmentScreenState {
    object Loading: ShipmentScreenState()

    data class DataFetched(val list: List<Shipment>): ShipmentScreenState() {
        override fun equals(other: Any?): Boolean {
            return other is DataFetched
        }

        override fun hashCode(): Int {
            return list.hashCode()
        }
    }

    object NoData: ShipmentScreenState()
}