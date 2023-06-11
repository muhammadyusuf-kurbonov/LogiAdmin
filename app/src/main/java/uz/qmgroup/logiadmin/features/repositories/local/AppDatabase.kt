package uz.qmgroup.logiadmin.features.repositories.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import uz.qmgroup.logiadmin.features.transports.TransportsDataSource
import uz.qmgroup.logiadmin.features.transports.models.Transport


@Database(
    entities = [LocalTransportEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase(), TransportsDataSource {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context,
                            AppDatabase::class.java,
                            "logiadmin-main.db"
                        ).build()
                    }
                }
            }
            return INSTANCE
        }
    }

    protected abstract val transportsDao: LocalTransportsDao

    override fun getTransports(query: String): Flow<List<Transport>> =
        transportsDao.searchTransports(query).map { it.map { transport -> transport.toDomain() } }

    override suspend fun saveTransport(transport: Transport): Transport =
        withContext(Dispatchers.IO) {
            transportsDao.insert(transport.toLocalEntity()).toDomain()
        }

    override suspend fun getByIds(ids: List<Long>): Map<Long, Transport?> =
        withContext(Dispatchers.IO) {
            transportsDao.getTransports(ids)
                .map { transports -> transports.toDomain() }
                .associateByTo(hashMapOf(), Transport::transportId)
        }

    override suspend fun updateTransport(transport: Transport): Transport =
        withContext(Dispatchers.IO) {
            transportsDao.update(transport.toLocalEntity()).toDomain()
        }

    override suspend fun deleteTransport(transport: Transport): Transport =
        withContext(Dispatchers.IO) {
            transportsDao.deleteTransport(transport.toLocalEntity())
            transport
        }
}