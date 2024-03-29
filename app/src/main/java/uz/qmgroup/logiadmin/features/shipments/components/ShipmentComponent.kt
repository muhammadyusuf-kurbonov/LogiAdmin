package uz.qmgroup.logiadmin.features.shipments.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.qmgroup.logiadmin.R
import uz.qmgroup.logiadmin.components.ConfirmButtonWrapper
import uz.qmgroup.logiadmin.features.shipments.models.Shipment
import uz.qmgroup.logiadmin.features.shipments.models.ShipmentStatus
import uz.qmgroup.logiadmin.features.transports.models.Transport
import uz.qmgroup.logiadmin.features.transports.models.TransportType
import uz.qmgroup.logiadmin.ui.theme.LogiAdminTheme
import java.text.NumberFormat

@Composable
fun getStatusLabels() = mapOf(
    ShipmentStatus.CANCELLED to stringResource(R.string.Cancelled),
    ShipmentStatus.CREATED to stringResource(R.string.Created),
    ShipmentStatus.ASSIGNED to stringResource(R.string.Assigned),
    ShipmentStatus.ON_WAY to stringResource(R.string.On_way),
    ShipmentStatus.COMPLETED to stringResource(R.string.Completed),
    ShipmentStatus.UNKNOWN to stringResource(R.string.Unknown)
)

@Composable
fun ShipmentComponent(
    modifier: Modifier = Modifier,
    shipment: Shipment,
    isInProgress: Boolean,
    cancelShipment: () -> Unit,
    startShipment: () -> Unit,
    completeShipment: () -> Unit,
    requestDriverSelect: () -> Unit,
    callTheDriver: (phone: String) -> Unit = {},
) {
    Card(modifier = modifier
        .animateContentSize()
        .width(IntrinsicSize.Min), shape = RoundedCornerShape(16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "${shipment.pickoffPlace} - ${shipment.destinationPlace}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                val color = when (shipment.status) {
                    ShipmentStatus.CANCELLED -> MaterialTheme.colorScheme.error
                    ShipmentStatus.CREATED -> MaterialTheme.colorScheme.onPrimaryContainer
                    else -> MaterialTheme.colorScheme.secondary
                }

                CompositionLocalProvider(LocalContentColor provides color) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(15.dp)
                                .clip(CircleShape)
                                .background(LocalContentColor.current)
                        )

                        Text(
                            getStatusLabels()[shipment.status] ?: shipment.status.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.Shipment_id), style = MaterialTheme.typography.labelMedium)
                    Text(
                        "${shipment.orderPrefix}${shipment.orderId}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (!shipment.company.isNullOrEmpty()) {
                        Spacer(Modifier.height(8.dp))

                        Text(stringResource(R.string.Company), style = MaterialTheme.typography.labelMedium)
                        Text(
                            shipment.company,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }


                    AnimatedVisibility(shipment.transport != null) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))

                            Column {
                                Text(
                                    stringResource(R.string.Transport_number),
                                    style = MaterialTheme.typography.labelMedium
                                )

                                Text(
                                    shipment.transport?.transportNumber ?: stringResource(id = R.string.Loading),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Column {
                                    Text(
                                        stringResource(R.string.Driver_name),
                                        style = MaterialTheme.typography.labelMedium
                                    )

                                    Text(
                                        shipment.transport?.driverName ?: stringResource(id = R.string.Loading),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(stringResource(R.string.Cost), style = MaterialTheme.typography.labelMedium)
                    Text(
                        "${NumberFormat.getNumberInstance().format(shipment.price)} сум",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(8.dp))
                }
//                Image(
//                    modifier = Modifier.weight(4f).aspectRatio(1f),
//                    painter = painterResource("tent-truck.webp"),
//                    contentDescription = "Tent-truck",
//                    contentScale = ContentScale.Inside,
//                )

            }

            if (!isInProgress) {
                when (shipment.status) {
                    ShipmentStatus.CANCELLED -> {}

                    ShipmentStatus.CREATED -> {
                        Row(modifier = Modifier.align(Alignment.End)) {
                            ConfirmButtonWrapper(
                                onConfirmed = cancelShipment,
                                message = stringResource(R.string.Cancel_confirm),
                            ) {
                                TextButton(
                                    onClick = it.onClick
                                ) {
                                    Text(stringResource(R.string.Cancel))
                                }
                            }
                            Button(
                                onClick = requestDriverSelect
                            ) {
                                Text(stringResource(R.string.Assign_driver))
                            }
                        }
                    }

                    ShipmentStatus.ASSIGNED -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (shipment.transport != null) {
                                OutlinedButton(onClick = { callTheDriver(shipment.transport.driverPhone) }) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = "",
                                        tint = LocalContentColor.current
                                    )
                                }
                            } else {
                                Box(modifier = Modifier)
                            }

                            Row {
                                ConfirmButtonWrapper(
                                    onConfirmed = cancelShipment,
                                    message = stringResource(R.string.Cancel_confirm),
                                ) {
                                    TextButton(
                                        onClick = it.onClick
                                    ) {
                                        Text(stringResource(R.string.Cancel))
                                    }
                                }
                                Button(onClick = startShipment) {
                                    Text(stringResource(R.string.Send))
                                }
                            }
                        }
                    }

                    ShipmentStatus.ON_WAY -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (shipment.transport != null) {
                                OutlinedButton(onClick = { callTheDriver(shipment.transport.driverPhone) }) {
                                    Icon(
                                        imageVector = Icons.Default.Phone,
                                        contentDescription = "",
                                        tint = LocalContentColor.current
                                    )
                                }
                            } else {
                                Box(modifier = Modifier)
                            }

                            Row {
                                ConfirmButtonWrapper(
                                    onConfirmed = cancelShipment,
                                    message = stringResource(R.string.Cancel_confirm),
                                ) {
                                    TextButton(
                                        onClick = it.onClick
                                    ) {
                                        Text(stringResource(R.string.Cancel))
                                    }
                                }
                                Button(onClick = completeShipment) {
                                    Text(stringResource(R.string.Complete))
                                }
                            }
                        }
                    }

                    ShipmentStatus.COMPLETED -> {}

                    ShipmentStatus.UNKNOWN -> {}
                }
            } else {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Preview()
@Composable
fun PreviewShipmentCreatedComponent() {
    LogiAdminTheme {
        ShipmentComponent(
            modifier = Modifier.fillMaxWidth(), Shipment(
                orderId = 15,
                note = "Test shipment",
                orderPrefix = "CC",
                transportId = null,
                status = ShipmentStatus.CREATED,
                pickoffPlace = "Tashkent",
                destinationPlace = "Chiroqchi",
                price = 5000000.0,
                author = "Diyorbek",
                transport = null,
                company = "Umid OOO",
                databaseId = ""
            ),
            isInProgress = false,
            cancelShipment = {},
            requestDriverSelect = {},
            startShipment = {},
            completeShipment = {}
        )
    }
}


@Preview()
@Composable
fun PreviewShipmentAssignedComponent() {
    LogiAdminTheme {
        ShipmentComponent(
            modifier = Modifier.fillMaxWidth(), Shipment(
                orderId = 15,
                note = "Test shipment",
                orderPrefix = "CC",
                transportId = null,
                status = ShipmentStatus.ASSIGNED,
                pickoffPlace = "Tashkent",
                destinationPlace = "Chiroqchi",
                price = 5000000.0,
                author = "Diyorbek",
                transport = Transport(
                    transportId = 0,
                    driverName = "Marat aka",
                    driverPhone = "+998913975538",
                    transportNumber = "40L544KA",
                    type = TransportType.REFRIGERATOR_MODE
                ),
                company = "Umid OOO"
            ),
            isInProgress = false,
            cancelShipment = {},
            requestDriverSelect = {},
            startShipment = {},
            completeShipment = {}
        )
    }
}

@Preview()
@Composable
fun PreviewShipmentComponent() {
    LogiAdminTheme {
        ShipmentComponent(
            modifier = Modifier.fillMaxWidth(), Shipment(
                orderId = 15,
                note = "Test shipment",
                orderPrefix = "CC",
                transportId = null,
                status = ShipmentStatus.CANCELLED,
                pickoffPlace = "Tashkent",
                destinationPlace = "Chiroqchi",
                price = 5000000.0,
                author = "Diyorbek",
                transport = null,
                company = "Umid OOO"
            ),
            isInProgress = false,
            cancelShipment = {},
            requestDriverSelect = {},
            startShipment = {},
            completeShipment = {}
        )
    }
}

@Preview()
@Composable
fun PreviewShipmentOnWayComponent() {
    LogiAdminTheme {
        ShipmentComponent(
            modifier = Modifier.fillMaxWidth(), Shipment(
                orderId = 15,
                note = "Test shipment",
                orderPrefix = "CC",
                transportId = null,
                status = ShipmentStatus.ON_WAY,
                pickoffPlace = "Tashkent",
                destinationPlace = "Chiroqchi",
                price = 5000000.0,
                author = "Diyorbek",
                transport = Transport(
                    transportId = 0,
                    driverName = "Marat aka",
                    driverPhone = "+998913975538",
                    transportNumber = "40L544KA",
                    type = TransportType.REFRIGERATOR_MODE
                ),
                company = "Umid OOO"
            ),
            isInProgress = false,
            cancelShipment = {},
            requestDriverSelect = {},
            startShipment = {},
            completeShipment = {}
        )
    }
}

@Preview()
@Composable
fun PreviewShipmentCompletedComponent() {
    LogiAdminTheme {
        ShipmentComponent(
            modifier = Modifier.fillMaxWidth(), Shipment(
                orderId = 15,
                note = "Test shipment",
                orderPrefix = "CC",
                transportId = null,
                status = ShipmentStatus.COMPLETED,
                pickoffPlace = "Tashkent",
                destinationPlace = "Chiroqchi",
                price = 5000000.0,
                author = "Diyorbek",
                transport = Transport(
                    transportId = 0,
                    driverName = "Marat aka",
                    driverPhone = "+998913975538",
                    transportNumber = "40L544KA",
                    type = TransportType.REFRIGERATOR_MODE
                ),
                company = "Umid OOO"
            ),
            isInProgress = false,
            cancelShipment = {},
            requestDriverSelect = {},
            startShipment = {},
            completeShipment = {}
        )
    }
}

@Preview()
@Composable
fun PreviewShipmentUnknownComponent() {
    LogiAdminTheme {
        ShipmentComponent(
            modifier = Modifier.fillMaxWidth(), Shipment(
                orderId = 15,
                note = "Test shipment",
                orderPrefix = "CC",
                transportId = null,
                status = ShipmentStatus.UNKNOWN,
                pickoffPlace = "Tashkent",
                destinationPlace = "Chiroqchi",
                price = 5000000.0,
                author = "Diyorbek",
                transport = Transport(
                    transportId = 0,
                    driverName = "Marat aka",
                    driverPhone = "+998913975538",
                    transportNumber = "40L544KA",
                    type = TransportType.REFRIGERATOR_MODE
                ),
                company = "Umid OOO"
            ),
            isInProgress = false,
            cancelShipment = {},
            requestDriverSelect = {},
            startShipment = {},
            completeShipment = {}
        )
    }
}
