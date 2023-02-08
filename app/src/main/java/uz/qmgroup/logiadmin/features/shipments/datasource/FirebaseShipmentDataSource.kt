package uz.qmgroup.logiadmin.features.shipments.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import uz.qmgroup.logiadmin.features.shipments.models.Shipment

class FirebaseShipmentDataSource(
    private val database: FirebaseFirestore
) : ShipmentDataSource {
    override fun getShipments(query: String): Flow<List<Shipment>> = callbackFlow {
        val registration = database.collection("shipments").orderBy("status").limit(100)
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
            database.collection("shipments").document(entity.id)
        else
            database.collection("shipments").document()

        docReference.set(entity.copy(id = docReference.id))
    }
}