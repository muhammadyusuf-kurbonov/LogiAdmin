package uz.qmgroup.logiadmin.features.transports.remotestore.datasource

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import uz.qmgroup.logiadmin.features.transports.models.Transport
import uz.qmgroup.logiadmin.features.transports.models.TransportType

@Keep
data class FirebaseTransportEntity(
    val id: String = "",

    val transportId: Long = -1,
    val driverFullName: String = "",
    val driverPhone: String = "",
    val transportNumber: String = "",
    val type: TransportType = TransportType.TENTOVKA,

    val createdAt: Timestamp = Timestamp.now(),
    val updatedAt: Timestamp = Timestamp.now(),
)

fun FirebaseTransportEntity.toDomainModel(): Transport {
    return Transport(
        databaseId = id,
        transportId = transportId,
        driverName = driverFullName,
        driverPhone = driverPhone,
        createdAt = createdAt.toDate(),
        updatedAt = updatedAt.toDate(),
        transportNumber = transportNumber,
        type = type,
    )
}

fun Transport.toFirebaseEntity(): FirebaseTransportEntity {
    return FirebaseTransportEntity(
        id = databaseId ?: "",
        transportId = transportId,
        driverFullName = driverName,
        driverPhone = driverPhone,
        transportNumber = transportNumber,
        type = type,
        createdAt = Timestamp(createdAt),
        updatedAt = Timestamp(updatedAt)
    )
}