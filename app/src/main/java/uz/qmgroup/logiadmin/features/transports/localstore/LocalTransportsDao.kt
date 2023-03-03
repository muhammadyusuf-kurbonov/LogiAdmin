package uz.qmgroup.logiadmin.features.transports.localstore

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@Dao
abstract class LocalTransportsDao {
    @Insert
    protected abstract suspend fun insertTransport(entity: LocalTransportEntity): LocalTransportEntity
    @Update
    protected abstract suspend fun updateTransport(entity: LocalTransportEntity)

    suspend fun insert(entity: LocalTransportEntity) = withContext(Dispatchers.IO) {
        insertTransport(
            entity.copy(
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
        )
    }
    suspend fun update(entity: LocalTransportEntity) = withContext(Dispatchers.IO) {
        updateTransport(
            entity.copy(
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    @Query("SELECT * FROM LocalTransportEntity WHERE driverFullName LIKE '%'||:query||'%' OR driverPhone LIKE '%'||:query||'%'")
    abstract fun searchTransports(query: String): Flow<List<LocalTransportEntity>>

    @Query("SELECT * FROM LocalTransportEntity WHERE id IN (:queryList)")
    abstract fun getTransports(queryList: List<Long>): List<LocalTransportEntity>
}