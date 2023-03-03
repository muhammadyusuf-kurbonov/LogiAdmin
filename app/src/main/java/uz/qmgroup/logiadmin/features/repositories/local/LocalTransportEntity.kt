package uz.qmgroup.logiadmin.features.repositories.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.qmgroup.logiadmin.features.transports.models.Transport
import uz.qmgroup.logiadmin.features.transports.models.TransportType
import java.util.Date

@Entity
data class LocalTransportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val remoteTransportId: String? = null,
    val driverFullName: String,
    val driverPhone: String,
    val transportNumber: String,
    val type: TransportType = TransportType.TENTOVKA,

    val createdAt: Long,
    val updatedAt: Long = System.currentTimeMillis()
)

fun LocalTransportEntity.toDomain(): Transport = Transport(
    transportId = id,
    driverName = driverFullName,
    driverPhone = driverPhone,
    transportNumber = transportNumber,
    type = type,
    createdAt = Date(createdAt),
    updatedAt = Date(updatedAt),
    databaseId = remoteTransportId
)

fun Transport.toLocalEntity(): LocalTransportEntity = LocalTransportEntity(
    updatedAt = updatedAt.time,
    createdAt = createdAt.time,
    remoteTransportId = databaseId,
    driverFullName = driverName,
    driverPhone = driverPhone,
    transportNumber = transportNumber,
    type = type,
    id = transportId
)