package uz.qmgroup.logiadmin.features.transports.remotestore.datasource

import kotlinx.coroutines.flow.Flow
import uz.qmgroup.logiadmin.features.transports.models.Transport

interface TransportsDataSource {
    fun getTransports(query: String): Flow<List<Transport>>

    suspend fun saveTransport(transport: Transport): Transport

    suspend fun getByIds(ids: List<Long>): Map<Long, Transport?>
}