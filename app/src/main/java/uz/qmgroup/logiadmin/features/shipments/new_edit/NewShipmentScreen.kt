package uz.qmgroup.logiadmin.features.shipments.new_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import uz.qmgroup.logiadmin.features.app.LocalAppPortalsProvider
import uz.qmgroup.logiadmin.features.shipments.components.ShipmentForm
import uz.qmgroup.logiadmin.features.shipments.models.Shipment
import uz.qmgroup.logiadmin.features.shipments.models.ShipmentStatus

@Composable
fun NewShipmentScreen(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    viewModel: ShipmentAddEditViewModel = koinViewModel()
) {
    val portals = LocalAppPortalsProvider.current
    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    val currentState by viewModel.state.collectAsState()
    var shipment by remember {
        mutableStateOf(
            Shipment(
                orderId = 0,
                note = "",
                orderPrefix = "",
                company = "",
                transportId = null,
                transport = null,
                status = ShipmentStatus.CREATED,
                pickoffPlace = "",
                destinationPlace = "",
                price = 0.0,
                author = "",
                databaseId = null,
            )
        )
    }

    portals.titleBarTrailingProvider = {
        TextButton(onClick = {
            viewModel.save(shipment)
        }) {
            Text("Сохранить")
        }
    }

    when (currentState) {
        ShipmentEditScreenState.Default, ShipmentEditScreenState.SavePending -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Divider()
                if (currentState == ShipmentEditScreenState.SavePending)
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

                ShipmentForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    onShipmentChange = { shipment = it }
                )
            }
        }

        is ShipmentEditScreenState.SaveFailed -> {}
        ShipmentEditScreenState.SaveCompleted -> {
            LaunchedEffect(key1 = Unit) {
                onDismissRequest()
            }
        }

        else -> {}
    }
}