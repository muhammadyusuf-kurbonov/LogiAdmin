package uz.qmgroup.logiadmin.features.startshipment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.koin.androidx.compose.koinViewModel
import uz.qmgroup.logiadmin.R
import uz.qmgroup.logiadmin.features.shipments.models.Shipment
import uz.qmgroup.logiadmin.features.startshipment.components.ReceiverCompanyInputForm

@Composable
fun StartShipmentDialog(
    onDismissRequest: () -> Unit,
    shipment: Shipment,
    viewModel: StartShipmentViewModel = koinViewModel()
) {
    var companyName by remember(shipment) {
        mutableStateOf(shipment.company)
    }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = state) {
        if (state == StartShipmentDialogState.Updated)
            onDismissRequest()
    }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        OutlinedCard {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.Info_of_receiver_company),
                    style = MaterialTheme.typography.titleMedium
                )

                ReceiverCompanyInputForm(
                    initialCompanyName = companyName.orEmpty(),
                    onCompanyNameChange = {
                        companyName = it
                    }
                )

                Button(
                    modifier = Modifier.align(Alignment.End),
                    onClick = { viewModel.updateStatusToSent(shipment, companyName.orEmpty()) },
                    enabled = state != StartShipmentDialogState.Saving && !companyName.isNullOrEmpty()
                ) {
                    Text(text = stringResource(id = R.string.Save))
                }
            }
        }
    }
}