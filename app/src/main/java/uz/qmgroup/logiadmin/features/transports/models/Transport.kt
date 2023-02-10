package uz.qmgroup.logiadmin.features.transports.models

import java.util.Date

data class Transport(
    val transportId: Long,
    val driverName: String,
    val driverPhone: String,
    val transportNumber: String,
    val type: TransportType,
    val databaseId: String? = null,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date(),
)