package uz.qmgroup.logiadmin.features.transports.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.qmgroup.logiadmin.features.transports.models.Transport
import uz.qmgroup.logiadmin.features.transports.models.TransportType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransportForm(
    modifier: Modifier = Modifier,
    onTransportChange: (Transport) -> Unit
) {
    val (driverFullName, onDriverFullNameChange) = remember { mutableStateOf("") }
    val (driverPhone, onDriverPhoneChange) = remember { mutableStateOf("") }
    val (transportNumber, onTransportNumberChange) = remember { mutableStateOf("") }

    val transport by remember {
        derivedStateOf {
            Transport(
                transportId = 0,
                driverName = driverFullName,
                driverPhone = driverPhone,
                transportNumber = transportNumber,
                type = TransportType.TENTOVKA
            )
        }
    }

    LaunchedEffect(key1 = transport) {
        onTransportChange(transport)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        OutlinedTextField(
            value = driverFullName,
            onValueChange = onDriverFullNameChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Driver full name")
            },
            singleLine = true
        )

        OutlinedTextField(
            value = driverPhone,
            onValueChange = onDriverPhoneChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Driver phone")
            },
            singleLine = true
        )

        OutlinedTextField(
            value = transportNumber,
            onValueChange = onTransportNumberChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Transport number")
            },
            singleLine = true
        )
    }
}