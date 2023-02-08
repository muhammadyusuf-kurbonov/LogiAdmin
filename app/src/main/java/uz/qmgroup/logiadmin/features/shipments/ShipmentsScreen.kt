package uz.qmgroup.logiadmin.features.shipments

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import uz.qmgroup.logiadmin.components.EmptyScreenContent
import uz.qmgroup.logiadmin.components.LoadingScreenContent
import uz.qmgroup.logiadmin.features.app.LocalSearchQueryProvider
import uz.qmgroup.logiadmin.features.shipments.components.NewOrderScreen
import uz.qmgroup.logiadmin.features.shipments.components.ShipmentComponent
import uz.qmgroup.logiadmin.ui.theme.LogiAdminTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShipmentsScreen(
    modifier: Modifier = Modifier,
    newScreenRequested: Boolean,
    newScreenRequestClosed: () -> Unit,
    viewModel: ShipmentViewModel = koinViewModel(),
) {
    val currentQuery = LocalSearchQueryProvider.current
    LaunchedEffect(key1 = viewModel, currentQuery) {
        viewModel.search(currentQuery)
    }

    val screenState by viewModel.state.collectAsState()

    AnimatedContent(targetState = screenState) { currentState ->
        if (newScreenRequested) {
            NewOrderScreen(
                modifier = modifier,
                onDismissRequest = newScreenRequestClosed
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
                    items(currentState.list) {
                        ShipmentComponent(
                            modifier = Modifier.fillMaxWidth(),
                            shipment = it,
                            isInProgress = false,
                            cancelShipment = {},
                            requestDriverSelect = {},
                            startShipment = {},
                            completeShipment = {}
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
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewShipmentScreen() {
    LogiAdminTheme {
        ShipmentsScreen(
            modifier = Modifier.fillMaxSize(),
            newScreenRequested = false,
            newScreenRequestClosed = {}
        )
    }
}