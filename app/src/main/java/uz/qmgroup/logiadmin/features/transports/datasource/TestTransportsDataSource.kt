package uz.qmgroup.logiadmin.features.transports.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.qmgroup.logiadmin.features.transports.models.Transport
import uz.qmgroup.logiadmin.features.transports.models.TransportType
import kotlin.random.Random

class TestTransportsDataSource: TransportsDataSource {
    override fun getTransports(query: String): Flow<List<Transport>> = flow {
        emit(
            (0 until 15).map {
                Transport(
                    transportId = it.toLong(),
                    driverName = "John Michael",
                    driverPhone = "+998905432214",
                    transportNumber = "40K45JA",
                    type = TransportType.values()[Random.nextInt(3)],
                    databaseId = null
                )
            }
        )
    }

    override suspend fun saveTransport(transport: Transport) {
        TODO("Not yet implemented")
    }

    override suspend fun getByIds(ids: List<Long>): Map<Long, Transport?> {
        TODO("Not yet implemented")
    }
}