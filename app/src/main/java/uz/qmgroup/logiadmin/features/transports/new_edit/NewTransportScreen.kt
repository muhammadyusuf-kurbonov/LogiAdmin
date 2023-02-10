package uz.qmgroup.logiadmin.features.transports.new_edit

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import uz.qmgroup.logiadmin.features.transports.components.TransportForm
import uz.qmgroup.logiadmin.features.transports.models.Transport
import uz.qmgroup.logiadmin.features.transports.models.TransportType

@Composable
fun NewTransportScreen(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    viewModel: TransportEditViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    val currentState by viewModel.state.collectAsState()
    var transport by remember {
        mutableStateOf(Transport(
            transportId = 0,
            driverName = "",
            driverPhone = "",
            transportNumber = "",
            type = TransportType.TENTOVKA,
            databaseId = null
        ))
    }

    when (currentState) {
        TransportEditScreenState.Default, TransportEditScreenState.SavePending -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Divider()
                if (currentState == TransportEditScreenState.SavePending)
                    LinearProgressIndicator()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TransportForm(
                        modifier = Modifier.fillMaxWidth(),
                        onTransportChange = { transport = it }
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
                                viewModel.save(transport)
                            },
                            enabled = currentState == TransportEditScreenState.Default
                        ) {
                            Text("Сохранить")
                        }
                    }
                }
            }
        }

        is TransportEditScreenState.SaveFailed -> {}
        TransportEditScreenState.SaveCompleted -> {
            LaunchedEffect(key1 = Unit) {
                onDismissRequest()
            }
        }
    }
}