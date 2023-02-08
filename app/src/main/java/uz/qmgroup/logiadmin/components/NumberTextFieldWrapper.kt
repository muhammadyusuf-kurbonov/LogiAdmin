package uz.qmgroup.logiadmin.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import uz.qmgroup.logiadmin.utils.NumberFormatVisualTransformer
import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale

class NumberTextFieldProps(
    val valueAsString: String,
    val onValueChanged: (String) -> Unit,
    val visualTransformer: NumberFormatVisualTransformer
)

@Composable
fun NumberTextFieldWrapper(
    value: Number,
    onValueChanged: (Number) -> Unit,
    content: @Composable (NumberTextFieldProps) -> Unit
) {
    val simpleNumberFormatter: NumberFormat = remember {
        NumberFormat.getInstance(Locale.forLanguageTag("uz")).apply {
            this.minimumFractionDigits = 2
        }
    }

    val updatedValue by rememberUpdatedState(value)
    val valueAsString by remember {
        derivedStateOf {
            simpleNumberFormatter.format(updatedValue)
        }
    }


    val onStringValueChanged: (String) -> Unit = remember {
        {
            try {
                onValueChanged(simpleNumberFormatter.parse(it.trim().trim('.', ',')) ?: 0.0)
            } catch (e: ParseException) {
                onValueChanged(0.0)
            }
        }
    }

    val props by remember {
        derivedStateOf {
            NumberTextFieldProps(
                valueAsString = valueAsString,
                onValueChanged = onStringValueChanged,
                visualTransformer = NumberFormatVisualTransformer(
                    simpleNumberFormatter,
                    originalValue = updatedValue
                )
            )
        }
    }
    content(props)
}