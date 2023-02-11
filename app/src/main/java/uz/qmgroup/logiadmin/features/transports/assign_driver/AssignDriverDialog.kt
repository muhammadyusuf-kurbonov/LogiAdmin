package uz.qmgroup.logiadmin.features.transports.assign_driver

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import org.koin.androidx.compose.koinViewModel
import uz.qmgroup.logiadmin.components.EmptyScreenContent
import uz.qmgroup.logiadmin.components.LoadingScreenContent
import uz.qmgroup.logiadmin.features.transports.TransportsScreenState
import uz.qmgroup.logiadmin.features.transports.TransportsViewModel
import uz.qmgroup.logiadmin.features.transports.components.TransportComponent
import uz.qmgroup.logiadmin.features.transports.models.Transport

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDriverDialog(
    onDismissRequest: () -> Unit,
    selectDriver: (Transport) -> Unit,
    viewModel: TransportsViewModel = koinViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(searchQuery) {
        viewModel.search(searchQuery)
    }

    val state by viewModel.state.collectAsState()

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card() {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Выберите гразовика", style = MaterialTheme.typography.titleSmall)

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Поиск") }
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
        }
    }
}