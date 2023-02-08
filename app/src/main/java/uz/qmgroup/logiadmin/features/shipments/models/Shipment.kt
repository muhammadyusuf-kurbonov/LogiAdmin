package uz.qmgroup.logiadmin.features.shipments.models

import java.util.Date

data class Shipment(
    val orderId: Long,
    val note: String,
    val orderPrefix: String,
    val company: String,
    val transportId: Long?,
    val transport: Transport?,
    val status: ShipmentStatus,
    val pickoffPlace: String,
    val destinationPlace: String,
    val price: Double,
    val author: String,
    val databaseId: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
)