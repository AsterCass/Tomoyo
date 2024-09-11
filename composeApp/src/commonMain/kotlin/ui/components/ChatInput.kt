package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.vectorResource
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.input_microphone
import tomoyo.composeapp.generated.resources.input_package
import tomoyo.composeapp.generated.resources.input_plus_square
import tomoyo.composeapp.generated.resources.input_send

enum class InputSelector {
    NONE,
    EMOJI,
    EXTRA,
    MAP,
    DM,
    PHONE,
    PICTURE
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserInput(
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    resetScroll: () -> Unit = {},
) {
    var currentInputSelector by rememberSaveable { mutableStateOf(InputSelector.NONE) }
    var textFieldFocusState by remember { mutableStateOf(false) }
    val dismissKeyboard = { currentInputSelector = InputSelector.NONE }
    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Center
    ) {
        UserInputText(
            textFieldValue = textState,
            onTextChanged = { textState = it },
            onSelectorChange = {
                currentInputSelector = it
            },
            keyboardShown = currentInputSelector == InputSelector.NONE && textFieldFocusState,
            onTextFieldFocused = { focused ->
                if (focused) {
                    currentInputSelector = InputSelector.NONE
                    resetScroll()
                }
                textFieldFocusState = focused
            },
            onMessageSent = {
                onMessageSent(textState.text)
                textState = TextFieldValue()
                resetScroll()
            },
            sendMessageEnabled = textState.text.isNotBlank(),
        )
        SelectorExpanded(
            onCloseRequested = dismissKeyboard,
            onTextAdded = { textState = textState.addText(it) },
            currentSelector = currentInputSelector
        )
    }

}

private fun TextFieldValue.addText(newString: String): TextFieldValue {
    val newText = this.text.replaceRange(
        this.selection.start,
        this.selection.end,
        newString
    )
    val newSelection = TextRange(
        start = newText.length,
        end = newText.length
    )

    return this.copy(text = newText, selection = newSelection)
}

@Composable
private fun SelectorExpanded(
    currentSelector: InputSelector,
    onCloseRequested: () -> Unit,
    onTextAdded: (String) -> Unit
) {
    if (currentSelector == InputSelector.NONE) return

    val focusRequester = FocusRequester()
    SideEffect {
        if (currentSelector == InputSelector.EMOJI || currentSelector == InputSelector.EXTRA) {
            focusRequester.requestFocus()
        }
    }

    Surface {
        when (currentSelector) {
            InputSelector.EMOJI -> EmojiSelector(onTextAdded, focusRequester)
            InputSelector.EXTRA -> ExtraSelector(onCloseRequested, focusRequester)
            else -> {
                FunctionalityNotAvailablePanel()
            }
        }
    }
}

@Composable
fun ExtraSelector(
    onCloseRequested: () -> Unit,
    focusRequester: FocusRequester
) {
    Column(
        modifier = Modifier
            .focusRequester(focusRequester)
            .focusTarget()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.input_package),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(50.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_package),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(50.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_package),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(50.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_package),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(50.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.input_package),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(50.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_package),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(50.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_package),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(50.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_package),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(50.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )

        }

    }
}

@Composable
fun FunctionalityNotAvailablePanel() {
    AnimatedVisibility(
        visibleState = remember { MutableTransitionState(false).apply { targetState = true } },
        enter = expandHorizontally() + fadeIn(),
        exit = shrinkHorizontally() + fadeOut()
    ) {
        Column(
            modifier = Modifier
                .height(320.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "not_available_subtitle",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "not_available_subtitle",
                modifier = Modifier.paddingFrom(FirstBaseline, before = 32.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


val KeyboardShownKey = SemanticsPropertyKey<Boolean>("KeyboardShownKey")
var SemanticsPropertyReceiver.keyboardShownProperty by KeyboardShownKey


@ExperimentalFoundationApi
@Composable
private fun UserInputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (TextFieldValue) -> Unit,
    onSelectorChange: (InputSelector) -> Unit,
    textFieldValue: TextFieldValue,
    keyboardShown: Boolean,
    onTextFieldFocused: (Boolean) -> Unit,
    onMessageSent: (String) -> Unit,
    sendMessageEnabled: Boolean,
) {

    Row(
        modifier = Modifier.fillMaxWidth().height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {


        Icon(
            imageVector = vectorResource(Res.drawable.input_microphone),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(5.dp))
                .clickable {
                    NotificationManager.createDialogAlert(
                        MainDialogAlert(
                            message = "xxxxxxxxxxxxxx",
                        )
                    )
                }
                .size(28.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )


        Box(modifier = Modifier.weight(1f)) {
            UserInputTextField(
                textFieldValue,
                onTextChanged,
                onTextFieldFocused,
                keyboardType,
                onMessageSent,
                Modifier.semantics {
                    keyboardShownProperty = keyboardShown
                }
            )
        }

        Icon(
            imageVector = vectorResource(Res.drawable.input_package),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(5.dp))
                .clickable {
                    onSelectorChange(InputSelector.EMOJI)
                }
                .size(28.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )

        if (sendMessageEnabled) {
            Icon(
                imageVector = vectorResource(Res.drawable.input_send),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable {
                        onMessageSent(textFieldValue.text)
                    }
                    .size(28.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
        } else {
            Icon(
                imageVector = vectorResource(Res.drawable.input_plus_square),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable {
                        onSelectorChange(InputSelector.EXTRA)
                    }
                    .size(28.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )

        }

    }
}

@Composable
private fun BoxScope.UserInputTextField(
    textFieldValue: TextFieldValue,
    onTextChanged: (TextFieldValue) -> Unit,
    onTextFieldFocused: (Boolean) -> Unit,
    keyboardType: KeyboardType,
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var lastFocusState by remember { mutableStateOf(false) }
    BasicTextField(
        value = textFieldValue,
        onValueChange = { onTextChanged(it) },
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(Color.White)
            .padding(vertical = 10.dp, horizontal = 10.dp)
            .align(Alignment.CenterStart)
            .onFocusChanged { state ->
                if (lastFocusState != state.isFocused) {
                    onTextFieldFocused(state.isFocused)
                }
                lastFocusState = state.isFocused
            },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions {
            if (textFieldValue.text.isNotBlank()) onMessageSent(textFieldValue.text)
        },
        maxLines = 1,
        cursorBrush = SolidColor(LocalContentColor.current),
        textStyle = LocalTextStyle.current.copy(color = LocalContentColor.current)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EmojiSelector(
    onTextAdded: (String) -> Unit,
    focusRequester: FocusRequester
) {
    Column(
        modifier = Modifier
            .focusRequester(focusRequester)
            .focusTarget()
    ) {
        FlowRow(
            modifier = Modifier.padding(vertical = 5.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.input_send),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable { onTextAdded("123") }
                    .size(28.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_send),
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable { onTextAdded("456") }
                    .size(28.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )

        }

    }
}