package uz.qmgroup.logiadmin.features.shipmentfilter

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import uz.qmgroup.logiadmin.features.shipmentfilter.filters.StatusFilter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShipmentFilterSheet(
    onDismissRequest: () -> Unit,
    filter: ShipmentFilter,
    onFilterChange: (ShipmentFilter) -> Unit
) {
    val filterUpdated by rememberUpdatedState(newValue = filter)

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        StatusFilter(
            currentState = filterUpdated.status,
            onFilterChange = { onFilterChange(filterUpdated.copy(status = it)) }
        )
    }
}
