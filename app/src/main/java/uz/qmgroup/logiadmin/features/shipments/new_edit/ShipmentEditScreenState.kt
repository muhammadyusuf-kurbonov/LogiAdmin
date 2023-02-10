package uz.qmgroup.logiadmin.features.shipments.new_edit

import uz.qmgroup.logiadmin.features.transports.models.Transport

sealed class ShipmentEditScreenState {
    object Default: ShipmentEditScreenState()

    object Fetching: ShipmentEditScreenState()

    class DataFetched(val transport: Transport): ShipmentEditScreenState()

    object SavePending: ShipmentEditScreenState()

    object SaveCompleted: ShipmentEditScreenState()

    class SaveFailed(val error: String): ShipmentEditScreenState()
}
