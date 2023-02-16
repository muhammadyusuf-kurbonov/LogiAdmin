package uz.qmgroup.logiadmin.features.transports.components

import android.telephony.PhoneNumberUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.qmgroup.logiadmin.features.transports.models.Transport
import uz.qmgroup.logiadmin.features.transports.models.TransportType
import uz.qmgroup.logiadmin.ui.theme.LogiAdminTheme
import java.util.Locale

val transportColors = mapOf(
    TransportType.TENTOVKA to Color(0xFFFFC107),
    TransportType.REFRIGERATOR_MODE to Color(0xFF00E676),
    TransportType.REFRIGERATOR_NO_MODE to Color(0xFFC2185B)
)

@Composable
fun TransportComponent(modifier: Modifier = Modifier, transport: Transport) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(transportColors[transport.type] ?: MaterialTheme.colorScheme.primary)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = transport.driverName, style = MaterialTheme.typography.bodyLarge)
            if (transport.driverPhone.isNotEmpty()) {
                Text(
                    text = try {
                        PhoneNumberUtils.formatNumber(
                            transport.driverPhone,
                            Locale.getDefault().country
                        )
                    } catch (exception: NullPointerException) {
                        transport.driverPhone
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(
            imageVector = Icons.Default.AllInbox,
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 16.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TransportTentComponentPreview() {
    LogiAdminTheme {
        TransportComponent(
            modifier = Modifier.fillMaxWidth(),
            transport = Transport(
                transportId = 0,
                driverName = "John Michael",
                driverPhone = "+998905432214",
                transportNumber = "40K45JA",
                type = TransportType.TENTOVKA,
                databaseId = null
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransportRefComponentPreview() {
    LogiAdminTheme {
        TransportComponent(
            modifier = Modifier.fillMaxWidth(),
            transport = Transport(
                transportId = 0,
                driverName = "John Michael",
                driverPhone = "+998905432214",
                transportNumber = "40K45JA",
                type = TransportType.REFRIGERATOR_MODE,
                databaseId = null
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransportRefNoModeComponentPreview() {
    LogiAdminTheme {
        TransportComponent(
            modifier = Modifier.fillMaxWidth(),
            transport = Transport(
                transportId = 0,
                driverName = "John Michael",
                driverPhone = "+998905432214",
                transportNumber = "40K45JA",
                type = TransportType.REFRIGERATOR_NO_MODE,
                databaseId = null
            )
        )
    }
}