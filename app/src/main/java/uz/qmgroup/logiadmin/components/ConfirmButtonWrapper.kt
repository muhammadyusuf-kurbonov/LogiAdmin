package uz.qmgroup.logiadmin.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

class ConfirmButtonWrapperProps(
    val onClick: () -> Unit
)

@Composable
fun ConfirmButtonWrapper(
    onConfirmed: () -> Unit,
    message: String = "Вы уверены?",
    content: @Composable (props: ConfirmButtonWrapperProps) -> Unit
) {
    var showConfirmDialog by remember {
        mutableStateOf(false)
    }
    val updatedOnConfirmAction by rememberUpdatedState(onConfirmed)

    val onClick = {
        showConfirmDialog = true
    }

    content(
        ConfirmButtonWrapperProps(onClick = onClick)
    )

    if (showConfirmDialog)
        Dialog(
            onDismissRequest = { showConfirmDialog = false },
            properties = DialogProperties(),
        ) {
            Surface {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = message, style = MaterialTheme.typography.bodyMedium)

                    Row(modifier = Modifier.align(Alignment.End)) {
                        TextButton(
                            onClick = { showConfirmDialog = false }
                        ) {
                            Text("Отменить")
                        }

                        TextButton(
                            onClick = {
                                updatedOnConfirmAction()
                                showConfirmDialog = false
                            }
                        ) {
                            Text("Подтвердить")
                        }
                    }
                }
            }
        }
}