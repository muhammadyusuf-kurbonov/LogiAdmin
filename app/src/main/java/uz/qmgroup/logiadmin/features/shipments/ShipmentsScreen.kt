package uz.qmgroup.logiadmin.features.shipments

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import uz.qmgroup.logiadmin.R
import uz.qmgroup.logiadmin.components.EmptyScreenContent
import uz.qmgroup.logiadmin.components.LoadingScreenContent
import uz.qmgroup.logiadmin.features.app.LocalAppPortalsProvider
import uz.qmgroup.logiadmin.features.shipmentfilter.ShipmentFilter
import uz.qmgroup.logiadmin.features.shipmentfilter.ShipmentFilterSheet
import uz.qmgroup.logiadmin.features.shipments.components.ShipmentComponent
import uz.qmgroup.logiadmin.features.shipments.models.Shipment
import uz.qmgroup.logiadmin.features.shipments.models.ShipmentStatus
import uz.qmgroup.logiadmin.features.shipments.new_edit.NewShipmentScreen
import uz.qmgroup.logiadmin.features.transports.assign_driver.SelectDriverDialog
import uz.qmgroup.logiadmin.ui.theme.LogiAdminTheme

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ShipmentsScreen(
    modifier: Modifier = Modifier,
    viewModel: ShipmentViewModel = koinViewModel(),
) {
    val portals = LocalAppPortalsProvider.current
    val context = LocalContext.current

    var searchQuery by remember {
        mutableStateOf("")
    }
    var filter by remember {
        mutableStateOf(ShipmentFilter(
            status = listOf(
                ShipmentStatus.CREATED,
                ShipmentStatus.ASSIGNED,
                ShipmentStatus.ON_WAY,
                ShipmentStatus.UNKNOWN,
                ShipmentStatus.COMPLETED
            )
        ))
    }
    var openCreateForm by remember {
        mutableStateOf(false)
    }
    var openFilterForm by remember {
        mutableStateOf(false)
    }
    var openAssignForm by remember {
        mutableStateOf<Shipment?>(null)
    }

    LaunchedEffect(key1 = Unit) {
        portals.fabPortal = {
            AnimatedVisibility(visible = !openCreateForm, enter = fadeIn(), exit = fadeOut()) {
                ExtendedFloatingActionButton(onClick = { openCreateForm = true }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.New_shipment)
                    )

                    Text(
                        text = stringResource(id = R.string.New_shipment)
                    )
                }
            }
        }

        portals.titleBarPortal = {
            AnimatedContent(openCreateForm) {
                if (it) {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = stringResource(id = R.string.New_shipment))
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = { openCreateForm = false }) {
                                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                            }
                        },
                        actions = {
                            portals.titleBarTrailingProvider?.invoke()
                        }
                    )
                } else {
                    TopAppBar(title = {
                        TextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 16.dp),
                            value = searchQuery,
                            onValueChange = { newQuery -> searchQuery = newQuery },
                            placeholder = {
                                Text(text = stringResource(R.string.search_a_shipment))
                            },
                            shape = RoundedCornerShape(99.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                errorIndicatorColor = Color.Transparent,
                            ),
                            textStyle = MaterialTheme.typography.bodyLarge,
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(R.string.search_a_shipment),
                                    modifier = Modifier.padding(16.dp)
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { openFilterForm = true },
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = if (filter.isClear()) Icons.Outlined.FilterAlt else Icons.Default.FilterAlt,
                                        contentDescription = stringResource(R.string.Filters)
                                    )
                                }
                            }
                        )
                    })
                }
            }
        }
    }

    LaunchedEffect(viewModel, searchQuery, filter) {
        delay(1000)
        viewModel.search(searchQuery, filter)
    }

    val screenState by viewModel.state.collectAsState()
    val currentState = screenState

    AnimatedContent(targetState = screenState::class) {
        if (openCreateForm) {
            NewShipmentScreen(
                modifier = modifier,
                onDismissRequest = { openCreateForm = false }
            )

            return@AnimatedContent
        }

        when (currentState) {
            is ShipmentScreenState.DataFetched -> {
                LazyColumn(
                    modifier = modifier
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(currentState.list, key = { it.orderId }) { shipment ->
                        ShipmentComponent(
                            modifier = Modifier.fillMaxWidth(),
                            shipment = shipment,
                            isInProgress = false,
                            cancelShipment = {
                                viewModel.cancel(shipment)
                            },
                            requestDriverSelect = {
                                openAssignForm = shipment
                            },
                            startShipment = {
                                viewModel.startShipment(shipment)
                            },
                            completeShipment = {
                                viewModel.completeShipment(shipment)
                            },
                            callTheDriver = { phone ->
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))

                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }

            ShipmentScreenState.Loading -> {
                LoadingScreenContent(modifier = modifier.fillMaxSize())
            }

            ShipmentScreenState.NoData -> {
                EmptyScreenContent(modifier = modifier.fillMaxSize())
            }
        }
    }

    if (openAssignForm != null) {
        SelectDriverDialog(
            onDismissRequest = {
                openAssignForm = null
            }
        ) {
            viewModel.assignDriver(openAssignForm!!, it)
        }
    }

    if (openFilterForm) {
        ShipmentFilterSheet(
            onDismissRequest = { openFilterForm = false },
            filter = filter,
            onFilterChange = { filter = it }
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewShipmentScreen() {
    LogiAdminTheme {
        ShipmentsScreen(
            modifier = Modifier.fillMaxSize(),
        )
    }
}