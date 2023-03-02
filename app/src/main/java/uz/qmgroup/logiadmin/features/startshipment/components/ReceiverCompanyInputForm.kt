package uz.qmgroup.logiadmin.features.startshipment.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay
import uz.qmgroup.logiadmin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiverCompanyInputForm(
    modifier: Modifier = Modifier,
    initialCompanyName: String,
    onCompanyNameChange: (String) -> Unit
) {
    val (company, onCompanyChange) = remember(initialCompanyName) {
        mutableStateOf(initialCompanyName)
    }

    LaunchedEffect(key1 = company) {
        delay(200)
        onCompanyNameChange(company)
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = company,
            onValueChange = onCompanyChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(stringResource(id = R.string.Company))
            },
            singleLine = true
        )
    }
}