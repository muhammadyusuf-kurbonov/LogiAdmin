package uz.qmgroup.logiadmin.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.NumberFormat

class NumberFormatVisualTransformer(
    private val numberFormat: NumberFormat = NumberFormat.getNumberInstance()
        .apply { minimumFractionDigits = 1 },
    private val originalValue: Number,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatter = numberFormat
        val formattedText = formatter.format(originalValue)
        val originalToTransformed = HashMap<Int, Int>()
        val transformedToOriginal = HashMap<Int, Int>()
        var originalIndex = 0
        var transformedIndex = 0

        while (originalIndex < text.length && transformedIndex < formattedText.length) {
            if (text[originalIndex] == formattedText[transformedIndex]) {
                originalToTransformed[originalIndex] = transformedIndex
                transformedToOriginal[transformedIndex] = originalIndex
                originalIndex += 1
                transformedIndex += 1
            } else {
                transformedIndex += 1
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return originalToTransformed[offset] ?: offset
            }

            override fun transformedToOriginal(offset: Int): Int {
                return transformedToOriginal[offset] ?: offset
            }

        }

        return TransformedText(AnnotatedString(formattedText, text.spanStyles), offsetMapping)
    }

}