package uz.qmgroup.logiadmin.features.shipmentfilter.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.qmgroup.logiadmin.R
import uz.qmgroup.logiadmin.ui.theme.LogiAdminTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandableRow(
    modifier: Modifier = Modifier,
    label: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onExpandedChange(!expanded) }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = label, style = MaterialTheme.typography.bodyLarge)

                AnimatedContent(targetState = expanded) {
                    Icon(
                        imageVector = if (it) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (it) stringResource(R.string.Collapse)
                        else stringResource(R.string.Expand),
                    )
                }
            }
        }

        AnimatedVisibility(expanded) {
            Column {
                content()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ExpandedRowPreview() {
    LogiAdminTheme {
        var state by remember {
            mutableStateOf(false)
        }
        ExpandableRow(label = "Test row", expanded = state, onExpandedChange = { state = it }) {
            Text(text = "Test #1")
            Text(text = "Test #1")
            Text(text = "Test #1")
            Text(text = "Test #1")
        }
    }
}