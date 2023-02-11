package uz.qmgroup.logiadmin.features.shipments.datasource

import com.google.firebase.Timestamp
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import uz.qmgroup.logiadmin.features.shipments.models.Shipment
import uz.qmgroup.logiadmin.features.shipments.models.ShipmentStatus
import uz.qmgroup.logiadmin.features.transports.datasource.TransportsDataSource
import uz.qmgroup.logiadmin.features.transports.models.Transport

class FirebaseShipmentDataSource(
    private val database: FirebaseFirestore,
    private val transportsDataSource: TransportsDataSource
) : ShipmentDataSource {
    companion object {
        const val COLLECTION_NAME = "shipments"
    }

    override fun getShipments(query: String): Flow<List<Shipment>> = callbackFlow {
        val registration = database.collection(COLLECTION_NAME).orderBy("status").limit(100)
            .addSnapshotListener { snapshot, error ->
                if (snapshot != null) {
                    val values = snapshot.toObjects<FirebaseShipmentEntity>()

                    val transportIds = values.mapNotNull { it.transportId }.distinct()
                    val shipments = values.map { it.toDomainModel() }

                    if (transportIds.isNotEmpty()) {
                        val transports =
                            runBlocking { transportsDataSource.getByIds(transportIds) }

                        trySend(shipments.map {
                            it.copy(
                                transport = transports[it.transportId]
                            )
                        })
                    } else {
                        trySend(shipments)
                    }
                }

                if (error != null) {
                    cancel(CancellationException("Firestore error", error))
                }
            }

        awaitClose { registration.remove() }
    }

    override suspend fun addNewShipment(shipment: Shipment) {
        val entity = shipment.toFirebaseEntity()

        val orderId =
            database.collection(COLLECTION_NAME).count().get(AggregateSource.SERVER)
                .await().count + 1

        val docReference = database.collection(COLLECTION_NAME).document()

        docReference.set(entity.copy(id = docReference.id, orderId = orderId)).await()
    }

    override suspend fun setStatus(shipment: Shipment, status: ShipmentStatus) {
        if (shipment.databaseId.isNullOrEmpty())
            throw IllegalArgumentException("Not saved Shipment can not be cancelled")

        val entity = shipment.toFirebaseEntity()
        database.collection(COLLECTION_NAME).document(entity.id)
            .update(
                mapOf(
                    "status" to status,
                    "updatedAt" to Timestamp.now()
                )
            ).await()
    }

    override suspend fun assignTransport(shipment: Shipment, transport: Transport) {
        if (shipment.databaseId.isNullOrEmpty())
            throw IllegalArgumentException("Not saved Shipment can not be updated")

        if (transport.databaseId.isNullOrEmpty())
            throw IllegalArgumentException("Not saved Transport can not be assigned")

        val entity = shipment.toFirebaseEntity()
        database.collection(COLLECTION_NAME).document(entity.id)
            .update(
                mapOf(
                    "status" to ShipmentStatus.ASSIGNED,
                    "updatedAt" to Timestamp.now(),
                    "transportId" to transport.transportId,
                )
            ).await()
    }
}