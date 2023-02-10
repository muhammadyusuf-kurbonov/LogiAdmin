package uz.qmgroup.logiadmin.features.app

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import uz.qmgroup.logiadmin.R
import uz.qmgroup.logiadmin.features.shipments.ShipmentsScreen
import uz.qmgroup.logiadmin.features.transports.TransportsScreen


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AppScreen(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinViewModel(),
) {
    val currentState by viewModel.state.collectAsState()
    val portals = rememberAppPortals()

    var searchQuery by remember {
        mutableStateOf("")
    }
    var newScreenRequired by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = newScreenRequired) {
        Log.d("LogiAdmin", "AppScreen: newScreenRequired changed to $newScreenRequired")
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            portals.titleBarPortal?.invoke()
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentState == AppScreenState.Shipments,
                    onClick = { viewModel.setScreen(AppScreenState.Shipments) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AllInbox,
                            contentDescription = stringResource(R.string.Shipments)
                        )
                    },
                    label = {
                        Text(text = stringResource(R.string.Shipments))
                    }
                )
                NavigationBarItem(
                    selected = currentState == AppScreenState.Transports,
                    onClick = { viewModel.setScreen(AppScreenState.Transports) },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = stringResource(R.string.Transports)
                        )
                    },
                    label = {
                        Text(text = stringResource(R.string.Transports))
                    }
                )
            }
        },
        floatingActionButton = {
            portals.fabPortal?.invoke()
        }
    ) { padding ->
        AnimatedContent(
            targetState = currentState,
            transitionSpec = {
                // Compare the incoming number with the previous number.
                if (targetState > initialState) {
                    // If the target number is larger, it slides up and fades in
                    // while the initial (smaller) number slides up and fades out.
                    slideInHorizontally { height -> height } with
                            slideOutHorizontally { height -> -height }
                } else {
                    // If the target number is smaller, it slides down and fades in
                    // while the initial number slides down and fades out.
                    slideInHorizontally { height -> -height } with
                            slideOutHorizontally { height -> height }
                }.using(
                    // Disable clipping since the faded slide-in/out should
                    // be displayed out of bounds.
                    SizeTransform(clip = false)
                )
            }
        ) {
            CompositionLocalProvider(
                LocalSearchQueryProvider provides searchQuery,
                LocalAppPortalsProvider provides portals
            ) {
                when (it) {
                    AppScreenState.Transports -> {
                        TransportsScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                        )
                    }

                    AppScreenState.Shipments -> {
                        ShipmentsScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding),
                        )
                    }
                }
            }
        }
    }
}