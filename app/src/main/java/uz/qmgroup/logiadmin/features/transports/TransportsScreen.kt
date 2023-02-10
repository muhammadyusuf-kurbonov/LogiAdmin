package uz.qmgroup.logiadmin.features.transports

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
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import uz.qmgroup.logiadmin.components.EmptyScreenContent
import uz.qmgroup.logiadmin.components.LoadingScreenContent
import uz.qmgroup.logiadmin.features.shipments.new_edit.NewOrderScreen
import uz.qmgroup.logiadmin.features.transports.components.TransportComponent

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TransportsScreen(
    modifier: Modifier = Modifier,
    newScreenRequested: Boolean = false,
    newScreenRequestClosed: () -> Unit = {},
    viewModel: TransportsViewModel = koinViewModel()
) {
    LaunchedEffect(key1 = viewModel) {
        viewModel.search("")
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
            is TransportsScreenState.DataFetched -> {
                LazyColumn(
                    modifier = modifier
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(currentState.list) {
                        TransportComponent(
                            modifier = Modifier.fillMaxWidth(),
                            transport = it
                        )
                    }
                }
            }

            TransportsScreenState.Loading -> {
                LoadingScreenContent(modifier = modifier.fillMaxSize())
            }

            TransportsScreenState.NoData -> {
                EmptyScreenContent(modifier = modifier.fillMaxSize())
            }
        }
    }
}