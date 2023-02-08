package uz.qmgroup.logiadmin.features.shipments.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import uz.qmgroup.logiadmin.features.shipments.ShipmentAddEditViewModel
import uz.qmgroup.logiadmin.features.shipments.ShipmentEditScreenState
import uz.qmgroup.logiadmin.features.shipments.models.Shipment
import uz.qmgroup.logiadmin.features.shipments.models.ShipmentStatus

@Composable
fun NewOrderScreen(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    viewModel: ShipmentAddEditViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    val currentState by viewModel.state.collectAsState()
    val (pickupAddress, onPickupAddressChange) = remember { mutableStateOf("") }
    val (destinationAddress, onDestinationAddressChange) = remember { mutableStateOf("") }
    val (orderPrefix, onOrderPrefixChange) = remember { mutableStateOf("") }
    val (company, onCompanyChange) = remember { mutableStateOf("") }
    val (price, onPriceChange) = remember { mutableStateOf(0.0) }

    when (currentState) {
        ShipmentEditScreenState.Default, ShipmentEditScreenState.SavePending -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Divider()
                if (currentState == ShipmentEditScreenState.SavePending)
                    LinearProgressIndicator()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ShipmentForm(
                        modifier = Modifier.fillMaxWidth(),
                        pickupAddress = pickupAddress,
                        onPickupAddressChange = onPickupAddressChange,
                        destinationAddress = destinationAddress,
                        onDestinationAddressChange = onDestinationAddressChange,
                        orderType = orderPrefix,
                        onOrderTypeChanged = onOrderPrefixChange,
                        price = price,
                        onPriceChanged = onPriceChange,
                        company = company,
                        onCompanyChanged = onCompanyChange
                    )

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = onDismissRequest) {
                            Text("Отмена")
                        }

                        Button(
                            onClick = {
                                viewModel.save(
                                    Shipment(
                                        orderId = 0,
                                        orderPrefix = orderPrefix,
                                        note = "",
                                        transportId = 0,
                                        status = ShipmentStatus.CREATED,
                                        pickoffPlace = pickupAddress,
                                        destinationPlace = destinationAddress,
                                        price = price,
                                        author = "Diyorbek",
                                        transport = null,
                                        company = company,
                                        databaseId = ""
                                    )
                                )
                            },
                            enabled = currentState == ShipmentEditScreenState.Default
                        ) {
                            Text("Сохранить")
                        }
                    }
                }
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