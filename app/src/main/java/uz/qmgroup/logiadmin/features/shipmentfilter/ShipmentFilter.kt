package uz.qmgroup.logiadmin.features.shipmentfilter

import uz.qmgroup.logiadmin.features.shipments.models.ShipmentStatus
import java.util.Date

data class ShipmentFilter(
    val status: List<ShipmentStatus>? = null,
    val date: Pair<Date, Date>? = null
) {
    fun isClear(): Boolean {
        return status.isNullOrEmpty() && date == null
    }
}