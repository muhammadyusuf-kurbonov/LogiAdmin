package uz.qmgroup.logiadmin.features.transports.new_edit

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import uz.qmgroup.logiadmin.R
import uz.qmgroup.logiadmin.features.app.LocalAppPortalsProvider
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

    val portals = LocalAppPortalsProvider.current

    val currentState by viewModel.state.collectAsState()
    var transport by remember {
        mutableStateOf(
            Transport(
                transportId = 0,
                driverName = "",
                driverPhone = "",
                transportNumber = "",
                type = TransportType.TENTOVKA,
                databaseId = null
            )
        )
    }

    portals.titleBarTrailingProvider = {
        TextButton(onClick = {
            viewModel.save(transport)
        }) {
            Text(stringResource(id = R.string.Save))
        }
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
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

                TransportForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    onTransportChange = {
                        transport = it
                    }
                )
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