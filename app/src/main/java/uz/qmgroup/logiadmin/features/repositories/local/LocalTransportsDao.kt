package uz.qmgroup.logiadmin.features.repositories.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Dao
abstract class LocalTransportsDao {
    @Query("SELECT * FROM LocalTransportEntity WHERE id = :id")
    abstract suspend fun getTransportById(id: Long): LocalTransportEntity

    @Insert
    protected abstract suspend fun insertTransport(entity: LocalTransportEntity): Long

    @Upsert
    protected abstract suspend fun updateTransport(entity: LocalTransportEntity)

    suspend fun insert(entity: LocalTransportEntity): LocalTransportEntity = withContext(Dispatchers.IO) {
        val newId = insertTransport(
            entity.copy(
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
        getTransportById(newId)
    }
    suspend fun update(entity: LocalTransportEntity): LocalTransportEntity = withContext(Dispatchers.IO) {
        updateTransport(
            entity.copy(
                updatedAt = System.currentTimeMillis()
            )
        )

        getTransportById(entity.id)
    }

    @Query("SELECT * FROM LocalTransportEntity WHERE driverFullName LIKE '%'||:query||'%' OR driverPhone LIKE '%'||:query||'%'")
    abstract fun searchTransports(query: String): Flow<List<LocalTransportEntity>>

    @Query("SELECT * FROM LocalTransportEntity WHERE id IN (:queryList)")
    abstract fun getTransports(queryList: List<Long>): List<LocalTransportEntity>

    @Delete
    abstract suspend fun deleteTransport(transportEntity: LocalTransportEntity)
}