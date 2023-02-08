package uz.qmgroup.logiadmin.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uz.qmgroup.logiadmin.R
import uz.qmgroup.logiadmin.ui.theme.LogiAdminTheme

@Composable
fun EmptyScreenContent(modifier: Modifier = Modifier) {
    Surface {
        Box(modifier = modifier) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(R.string.No_data_found),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
fun PreviewEmptyScreenContent() {
    LogiAdminTheme {
        EmptyScreenContent(
            modifier = Modifier.fillMaxSize()
        )
    }
}