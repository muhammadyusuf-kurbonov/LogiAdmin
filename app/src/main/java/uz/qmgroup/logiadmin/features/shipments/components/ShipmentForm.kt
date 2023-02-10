package uz.qmgroup.logiadmin.features.shipments.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uz.qmgroup.logiadmin.components.NumberTextFieldWrapper
import uz.qmgroup.logiadmin.features.shipments.models.Shipment
import uz.qmgroup.logiadmin.features.shipments.models.ShipmentStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipmentForm(
    modifier: Modifier = Modifier,
    onShipmentChange: (Shipment) -> Unit
) {
    val (pickupAddress, onPickupAddressChange) = remember { mutableStateOf("") }
    val (destinationAddress, onDestinationAddressChange) = remember { mutableStateOf("") }
    val (company, onCompanyChange) = remember { mutableStateOf("") }
    val (price, onPriceChange) = remember { mutableStateOf(0.0) }

    val shipment by remember(
        pickupAddress,
        destinationAddress,
        price,
        company
    ) {
        derivedStateOf {
            Shipment(
                orderId = 0,
                orderPrefix = "M-",
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
        }
    }

    LaunchedEffect(key1 = shipment) {
        onShipmentChange(shipment)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        OutlinedTextField(
            value = pickupAddress,
            onValueChange = onPickupAddressChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Откуда")
            },
            singleLine = true
        )

        OutlinedTextField(
            value = destinationAddress,
            onValueChange = onDestinationAddressChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Куда")
            },
            singleLine = true
        )

        OutlinedTextField(
            value = company,
            onValueChange = onCompanyChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Company")
            },
            singleLine = true
        )

        NumberTextFieldWrapper(
            value = price,
            onValueChanged = {
                onPriceChange(it.toDouble())
            }
        ) {
            OutlinedTextField(
                value = it.valueAsString,
                onValueChange = it.onValueChanged,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Стоимость")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                trailingIcon = {
                    Text(text = "UZS")
                },
                visualTransformation = it.visualTransformer
            )
        }
    }
}