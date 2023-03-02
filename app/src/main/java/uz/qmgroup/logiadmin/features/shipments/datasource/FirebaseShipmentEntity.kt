package uz.qmgroup.logiadmin.features.shipments.datasource

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import uz.qmgroup.logiadmin.features.shipments.models.Shipment
import uz.qmgroup.logiadmin.features.shipments.models.ShipmentStatus

@Keep
data class FirebaseShipmentEntity(
    val id: String = "",
    val orderId: Long = -1L,
    val note: String = "",
    val orderPrefix: String = "",
    val company: String? = null,
    val transportId: Long? = null,
    val status: ShipmentStatus = ShipmentStatus.UNKNOWN,
    val pickoffPlace: String = "",
    val destinationPlace: String = "",
    val price: Double = -1.0,
    val author: String = "",
    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
)

fun FirebaseShipmentEntity.toDomainModel(): Shipment {
    return Shipment(
        databaseId = id,
        orderId = orderId,
        note = note,
        orderPrefix = orderPrefix,
        company = company,
        transportId = transportId,
        transport = null,
        status = status,
        pickoffPlace = pickoffPlace,
        destinationPlace = destinationPlace,
        price = price,
        author = author,
        createdAt = createdAt.toDate(),
        updatedAt = updatedAt.toDate()
    )
}

fun Shipment.toFirebaseEntity(): FirebaseShipmentEntity {
    return FirebaseShipmentEntity(
        id = databaseId ?: "",
        orderId = orderId,
        note = note,
        orderPrefix = orderPrefix,
        company = company,
        transportId = transportId,
        status = status,
        pickoffPlace = pickoffPlace,
        destinationPlace = destinationPlace,
        price = price,
        author = author,
        createdAt = Timestamp(createdAt),
        updatedAt = Timestamp(updatedAt)
    )
}