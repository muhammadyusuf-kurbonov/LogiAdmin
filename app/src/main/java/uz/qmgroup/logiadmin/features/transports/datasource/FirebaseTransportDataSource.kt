package uz.qmgroup.logiadmin.features.transports.datasource

import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uz.qmgroup.logiadmin.features.transports.models.Transport

class FirebaseTransportDataSource(private val database: FirebaseFirestore) : TransportsDataSource {
    companion object {
        const val COLLECTION_NAME = "transports"
    }

    override fun getTransports(query: String): Flow<List<Transport>> = callbackFlow {
        val registration = database.collection(COLLECTION_NAME).orderBy("createdAt").limit(100)
            .addSnapshotListener { snapshot, error ->
                if (snapshot != null) {
                    val values = snapshot.toObjects<FirebaseTransportEntity>()

                    trySend(values.map { it.toDomainModel() })
                }

                if (error != null) {
                    cancel(CancellationException("Firestore error", error))
                }
            }

        awaitClose { registration.remove() }
    }

    override suspend fun saveTransport(transport: Transport) {
        val entity = transport.toFirebaseEntity()

        val transportId =
            database.collection(COLLECTION_NAME).count().get(
                AggregateSource.SERVER
            ).await().count + 1

        val docReference = database.collection(COLLECTION_NAME).document()

        docReference.set(entity.copy(id = docReference.id, transportId = transportId)).await()
    }

    override suspend fun getByIds(ids: List<Long>): Map<Long, Transport?> {
        val transports =
            database.collection(COLLECTION_NAME).whereIn("transportId", ids).get().await()
                .toObjects<FirebaseTransportEntity>()
        val hashMap = mutableMapOf<Long, Transport>()
        transports.forEach {
            hashMap[it.transportId] = it.toDomainModel()
        }
        return hashMap.toMap()
    }

}