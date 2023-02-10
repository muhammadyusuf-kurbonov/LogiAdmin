package uz.qmgroup.logiadmin.features.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class AppScaffoldPortals {
    var fabPortal by mutableStateOf<(@Composable () -> Unit)?>(null)

    var titleBarPortal by mutableStateOf<(@Composable () -> Unit)?>(null)
}

val LocalAppPortalsProvider = compositionLocalOf<AppScaffoldPortals> { throw NotImplementedError() }

@Composable
fun rememberAppPortals(): AppScaffoldPortals {
    return remember { AppScaffoldPortals() }
}