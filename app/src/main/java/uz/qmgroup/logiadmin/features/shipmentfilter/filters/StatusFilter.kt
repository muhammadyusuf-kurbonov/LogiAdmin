package uz.qmgroup.logiadmin.features.shipmentfilter.filters

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uz.qmgroup.logiadmin.R
import uz.qmgroup.logiadmin.features.shipmentfilter.components.ExpandableRow
import uz.qmgroup.logiadmin.features.shipments.components.getStatusLabels
import uz.qmgroup.logiadmin.features.shipments.models.ShipmentStatus

@Composable
fun StatusFilter(
    modifier: Modifier = Modifier,
    currentState: List<ShipmentStatus>?,
    onFilterChange: (List<ShipmentStatus>?) -> Unit
) {
    val enabledStatuses = remember {
        mutableStateListOf<ShipmentStatus>()
    }

    LaunchedEffect(key1 = currentState) {
        if (currentState != null) {
            enabledStatuses.clear()
            enabledStatuses.addAll(currentState)
        }
    }

    var expanded by remember {
        mutableStateOf(false)
    }
    ExpandableRow(
        modifier = modifier.wrapContentHeight(),
        label = stringResource(R.string.Status),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        LazyColumn {
            items(ShipmentStatus.values()) {status ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = enabledStatuses.contains(status), onCheckedChange = {
                        if (it) enabledStatuses.add(status)
                        else enabledStatuses.remove(status)

                        onFilterChange(enabledStatuses.toList())
                    })
                    Text(
                        text = getStatusLabels()[status] ?: "Unknown",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}