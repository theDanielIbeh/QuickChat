package com.example.quickchat.ui.presentation.screens.verification

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly

@Composable
fun OtpInputField(
    number: Int?,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onValueChanged: (Int?) -> Unit,
    onKeyboardBackspace: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by rememberSaveable(number, stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(
                text = number?.toString().orEmpty(),
                selection = TextRange(number?.toString()?.length ?: 0)
            )
        )
    }

    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .size(64.dp)
            .background(
                MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = if (text.text.isEmpty()) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                val newNumber = it.text
                if (newNumber.length <= 1 && newNumber.isDigitsOnly()) {
                    onValueChanged(newNumber.toIntOrNull())
                }
            },
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
            modifier = Modifier
                .padding(10.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                    onFocusChanged(it.isFocused)
                }
                .onKeyEvent {
                    val deletePressed = it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL
                    if (deletePressed && number == null) {
                        onKeyboardBackspace()
                        true
                    } else {
                        false
                    }
                },
            decorationBox = { innerTextField ->
                innerTextField()
                if (!isFocused && number == null) {
                    Text(
                        text = "-",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize()
                    )
                }
            }
        )
//        Text(
//            text = text.text,
//            style = MaterialTheme.typography.headlineMedium,
//            color = MaterialTheme.colorScheme.onSurface,
//            fontWeight = FontWeight.Bold
//        )
    }
}

@Preview
@Composable
fun OtpInputFieldPreview() {
    OtpInputField(
        number = null,
        focusRequester = remember { FocusRequester() },
        onFocusChanged = {},
        onValueChanged = {},
        onKeyboardBackspace = {}
    )
}