package uz.qmgroup.logiadmin.features.repositories

import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import uz.qmgroup.logiadmin.features.repositories.local.AppDatabase
import uz.qmgroup.logiadmin.features.repositories.remote.FirebaseTransportDataSource
import uz.qmgroup.logiadmin.features.repositories.remote.FirebaseTransportEntity
import uz.qmgroup.logiadmin.features.repositories.remote.toDomainModel
import uz.qmgroup.logiadmin.features.shipments.datasource.FirebaseShipmentDataSource
import uz.qmgroup.logiadmin.features.shipments.datasource.ShipmentDataSource
import uz.qmgroup.logiadmin.features.transports.TransportsDataSource
import uz.qmgroup.logiadmin.features.transports.models.Transport

class AppRepository(
    private val fireStore: FirebaseFirestore,
    private val database: AppDatabase
) : TransportsDataSource by database,
    ShipmentDataSource by FirebaseShipmentDataSource(fireStore, database) {

    override suspend fun saveTransport(transport: Transport): Transport {
        val transportsFirebase = FirebaseTransportDataSource(fireStore)
        return transportsFirebase.saveTransport(transport)
    }

    suspend fun initializeAndSynchronize(): Nothing = withContext(Dispatchers.Default) {
        Log.d("LogiAdmin", "Attaching synchronization proccess")

        val registration = fireStore.collection(FirebaseTransportDataSource.COLLECTION_NAME)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Firebase.crashlytics.recordException(error)
                    Log.e("LogiAdmin", error.message, error)
                    return@addSnapshotListener
                }
                val changes = value?.documentChanges

                Log.d("LogiAdmin", "$changes")

                if (changes.isNullOrEmpty()) return@addSnapshotListener

                for (change in changes) {
                    when (change.type) {
                        DocumentChange.Type.ADDED -> {
                            launch {
                                Log.d("LogiAdmin", "Inserting")
                                database.updateTransport(
                                    change.document.toObject(
                                        FirebaseTransportEntity::class.java
                                    ).toDomainModel()
                                )
                            }
                        }

                        DocumentChange.Type.MODIFIED -> {
                            launch {
                                Log.d("LogiAdmin", "Updated")
                                database.updateTransport(
                                    change.document.toObject(
                                        FirebaseTransportEntity::class.java
                                    ).toDomainModel()
                                )
                            }
                        }

                        DocumentChange.Type.REMOVED -> {
                            launch {
                                Log.d("LogiAdmin", "Deleted")
                                database.deleteTransport(
                                    change.document.toObject(
                                        FirebaseTransportEntity::class.java
                                    ).toDomainModel()
                                )
                            }
                        }
                    }
                }
            }

        Log.d("LogiAdmin", "Attached synchronization proccess")

        coroutineContext.job.invokeOnCompletion {
            registration.remove()

            Log.d("LogiAdmin", "Deleted synchronization proccess")
        }

        awaitCancellation()
    }

}