package uz.qmgroup.logiadmin.features.shipments.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uz.qmgroup.logiadmin.features.shipments.models.Shipment
import uz.qmgroup.logiadmin.features.shipments.models.ShipmentStatus

class FirebaseShipmentDataSource(
    private val database: FirebaseFirestore
) : ShipmentDataSource {
    companion object {
        const val COLLECTION_NAME = "shipments"
    }

    override fun getShipments(query: String): Flow<List<Shipment>> = callbackFlow {
        val registration = database.collection(COLLECTION_NAME).orderBy("status").limit(100)
            .addSnapshotListener { snapshot, error ->
                if (snapshot != null) {
                    val values = snapshot.toObjects<FirebaseShipmentEntity>()

                    trySend(values.map { it.toDomainModel() })
                }

                if (error != null) {
                    cancel(CancellationException("Firestore error", error))
                }
            }

        awaitClose { registration.remove() }
    }

    override suspend fun addNewShipment(shipment: Shipment) {
        val entity = shipment.toFirebaseEntity()
        val docReference = if (entity.id.isNotEmpty())
            database.collection(COLLECTION_NAME).document(entity.id)
        else
            database.collection(COLLECTION_NAME).document()

        docReference.set(entity.copy(id = docReference.id)).await()
    }

    override suspend fun cancelShipment(shipment: Shipment) {
        if (shipment.databaseId.isNullOrEmpty())
            throw IllegalArgumentException("Not saved Shipment can not be cancelled")

        val entity = shipment.toFirebaseEntity()
        database.collection(COLLECTION_NAME).document(entity.id)
            .set(entity.copy(status = ShipmentStatus.CANCELLED)).await()
    }
}