package uz.qmgroup.logiadmin.features.transports

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.qmgroup.logiadmin.features.transports.components.TransportComponent
import uz.qmgroup.logiadmin.features.transports.models.Transport
import uz.qmgroup.logiadmin.features.transports.models.TransportType
import kotlin.random.Random

@Composable
fun TransportsScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier.padding(horizontal = 8.dp)) {
        items(10) {
            TransportComponent(
                modifier = Modifier.fillMaxWidth(),
                transport = Transport(
                    transportId = 0,
                    driverName = "John Michael",
                    driverPhone = "+998905432214",
                    transportNumber = "40K45JA",
                    type = TransportType.values()[Random.nextInt(3)],
                    databaseId = null
                )
            )
        }
    }
}