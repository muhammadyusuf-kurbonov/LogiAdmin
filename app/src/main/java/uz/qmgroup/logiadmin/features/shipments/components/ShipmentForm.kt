package uz.qmgroup.logiadmin.features.shipments.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import uz.qmgroup.logiadmin.components.NumberTextFieldWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipmentForm(
    modifier: Modifier = Modifier,
    pickupAddress: String,
    onPickupAddressChange: (String) -> Unit,
    destinationAddress: String,
    onDestinationAddressChange: (String) -> Unit,
    orderType: String,
    onOrderTypeChanged: (String) -> Unit,
    company: String,
    onCompanyChanged: (String) -> Unit,
    price: Double,
    onPriceChanged: (Double) -> Unit
) {
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
            onValueChange = onCompanyChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Company")
            },
            singleLine = true
        )

        NumberTextFieldWrapper(
            value = price,
            onValueChanged = {
                onPriceChanged(it.toDouble())
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