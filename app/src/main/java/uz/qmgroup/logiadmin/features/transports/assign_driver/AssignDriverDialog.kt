package uz.qmgroup.logiadmin.features.transports.assign_driver

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.koin.androidx.compose.koinViewModel
import uz.qmgroup.logiadmin.R
import uz.qmgroup.logiadmin.components.EmptyScreenContent
import uz.qmgroup.logiadmin.components.LoadingScreenContent
import uz.qmgroup.logiadmin.features.transports.allscreen.TransportsScreenState
import uz.qmgroup.logiadmin.features.transports.allscreen.TransportsViewModel
import uz.qmgroup.logiadmin.features.transports.components.TransportComponent
import uz.qmgroup.logiadmin.features.transports.components.TransportForm
import uz.qmgroup.logiadmin.features.transports.models.Transport
import uz.qmgroup.logiadmin.features.transports.models.TransportType
import uz.qmgroup.logiadmin.features.transports.new_edit.TransportEditScreenState
import uz.qmgroup.logiadmin.features.transports.new_edit.TransportEditViewModel

@Composable
fun SelectDriverDialog(
    onDismissRequest: () -> Unit,
    selectDriver: (Transport) -> Unit
) {
    var currentTab by remember {
        mutableStateOf(0)
    }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        OutlinedCard {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .heightIn(min = 256.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TabRow(selectedTabIndex = currentTab) {
                    Tab(
                        selected = currentTab == 0,
                        onClick = { currentTab = 0 },
                        text = {
                            Text(
                                text = stringResource(R.string.Select_existing),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                    Tab(
                        selected = currentTab == 1,
                        onClick = { currentTab = 1 },
                        text = {
                            Text(
                                text = stringResource(R.string.Create_new),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }

                when (currentTab) {
                    0 -> SelectExisting(selectDriver, onDismissRequest)
                    1 -> CreateNewTransport(selectDriver, onDismissRequest)
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SelectExisting(
    selectDriver: (Transport) -> Unit,
    onDismissRequest: () -> Unit,
    viewModel: TransportsViewModel = koinViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        viewModel.search(searchQuery)
    }

    val state by viewModel.state.collectAsState()

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text(stringResource(id = R.string.Search_transport)) }
    )

    when (val currentState = state) {
        is TransportsScreenState.DataFetched -> {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(currentState.list) {
                    TransportComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectDriver(it)
                                onDismissRequest()
                            },
                        transport = it
                    )
                }
            }
        }

        TransportsScreenState.Loading -> {
            LoadingScreenContent(
                modifier = Modifier.fillMaxWidth()
            )
        }

        TransportsScreenState.NoData -> {
            EmptyScreenContent(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun CreateNewTransport(
    assign: (transport: Transport) -> Unit,
    onDismissRequest: () -> Unit,
    viewModel: TransportEditViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

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

    when (val state = currentState) {
        TransportEditScreenState.Default, TransportEditScreenState.SavePending -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                if (state == TransportEditScreenState.SavePending)
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

                TransportForm(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    onTransportChange = {
                        transport = it
                    }
                )

                Button(
                    modifier = Modifier.align(Alignment.End),
                    onClick = { viewModel.save(transport) },
                    enabled = state != TransportEditScreenState.SavePending
                ) {
                    Text(text = stringResource(id = R.string.Save))
                }
            }
        }

        is TransportEditScreenState.SaveFailed -> {}
        is TransportEditScreenState.SaveCompleted -> {
            LaunchedEffect(key1 = Unit) {
                assign(state.transport)
                onDismissRequest()
            }
        }
    }
}