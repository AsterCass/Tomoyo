package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import api.BaseApi
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.request.ComposableImageRequest
import com.github.panpf.sketch.resize.Precision
import com.github.panpf.sketch.resize.Scale
import constant.BaseResText
import constant.emojiList
import constant.enums.InputEmojiTabModel
import constant.kaomojiList
import data.UserState
import data.model.ChatScreenModel
import data.model.GlobalDataModel
import data.model.MainScreenModel
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.name
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.size
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.input_calendar
import tomoyo.composeapp.generated.resources.input_film
import tomoyo.composeapp.generated.resources.input_folder
import tomoyo.composeapp.generated.resources.input_globe
import tomoyo.composeapp.generated.resources.input_image
import tomoyo.composeapp.generated.resources.input_map
import tomoyo.composeapp.generated.resources.input_map_pin
import tomoyo.composeapp.generated.resources.input_microphone
import tomoyo.composeapp.generated.resources.input_phone
import tomoyo.composeapp.generated.resources.input_plus_square
import tomoyo.composeapp.generated.resources.input_send
import tomoyo.composeapp.generated.resources.input_settings
import tomoyo.composeapp.generated.resources.input_store
import tomoyo.composeapp.generated.resources.input_upload
import tomoyo.composeapp.generated.resources.input_users_three
import tomoyo.composeapp.generated.resources.main_chat_emoji_all
import tomoyo.composeapp.generated.resources.main_chat_emoji_recently

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
    inputText: String,
    updateInput: (String) -> Unit,
    onMessageSent: (String) -> Unit,
    modifier: Modifier = Modifier,
    resetScroll: () -> Unit = {},
) {
    var currentInputSelector by rememberSaveable { mutableStateOf(InputSelector.NONE) }
    var textFieldFocusState by remember { mutableStateOf(false) }
    val dismissKeyboard = { currentInputSelector = InputSelector.NONE }

    Column(
        modifier = modifier.imePadding(),
        verticalArrangement = Arrangement.Center
    ) {
        UserInputText(
            textFieldValue = inputText,
            onTextChanged = { updateInput(it) },
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
                onMessageSent(inputText)
                resetScroll()
            },
            sendMessageEnabled = inputText.isNotBlank(),
        )
        SelectorExpanded(
            sendNow = { message ->
                onMessageSent(message)
                resetScroll()
            },
            onCloseRequested = dismissKeyboard,
            onTextAdded = { updateInput(inputText.plus(it)) },
            currentSelector = currentInputSelector
        )
    }

}

@Composable
private fun SelectorExpanded(
    currentSelector: InputSelector,
    onCloseRequested: () -> Unit,
    onTextAdded: (String) -> Unit,
    sendNow: (String) -> Unit,
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
            InputSelector.EMOJI -> EmojiSelector(
                onTextAdded = onTextAdded,
                focusRequester = focusRequester,
                sendNow = sendNow,
            )
            InputSelector.EXTRA -> ExtraSelector(onCloseRequested, focusRequester, sendNow)
            else -> {
                FunctionalityNotAvailablePanel()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExtraSelector(
    onCloseRequested: () -> Unit,
    focusRequester: FocusRequester,
    sendNow: (String) -> Unit,
) {
    val mainModel: MainScreenModel = koinInject()
    val loadFileCoroutine = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .focusRequester(focusRequester)
            .focusTarget()
    ) {

        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(15.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            val launcher = rememberFilePickerLauncher(
                type = FileKitType.File(
                    extensions = listOf("webp", "png", "jpg", "jpeg", "gif")
                ),
            ) { file ->

                if (file != null) {
                    if (file.size() > 5000 * 1024) {
                        NotificationManager.createDialogAlert(
                            MainDialogAlert(
                                message = BaseResText.errorTooLargeUpload5000,
                                cancelOperationText = BaseResText.cancelBtn
                            )
                        )
                    } else {
                        loadFileCoroutine.launch {
                            val ret = BaseApi().uploadUserFile(
                                token = mainModel.userState.value.token,
                                2,
                                file.readBytes(),
                                file.name
                            )
                            if (ret.isNotEmpty()) {
                                sendNow(ret)
                            }
                        }
                    }
                }
            }

            Icon(
                imageVector = vectorResource(Res.drawable.input_image),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        launcher.launch()
                    }
                    .padding(15.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(32.dp),
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_folder),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        NotificationManager.createDialogAlert(
                            MainDialogAlert(
                                message = BaseResText.underDevelopment,
                                cancelOperationText = BaseResText.cancelBtn
                            )
                        )
                    }
                    .padding(15.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(32.dp),
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_calendar),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        NotificationManager.createDialogAlert(
                            MainDialogAlert(
                                message = BaseResText.underDevelopment,
                                cancelOperationText = BaseResText.cancelBtn
                            )
                        )
                    }
                    .padding(15.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(32.dp),
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_map),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        NotificationManager.createDialogAlert(
                            MainDialogAlert(
                                message = BaseResText.underDevelopment,
                                cancelOperationText = BaseResText.cancelBtn
                            )
                        )
                    }
                    .padding(15.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(32.dp),
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_film),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        NotificationManager.createDialogAlert(
                            MainDialogAlert(
                                message = BaseResText.underDevelopment,
                                cancelOperationText = BaseResText.cancelBtn
                            )
                        )
                    }
                    .padding(15.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(32.dp),
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_globe),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        NotificationManager.createDialogAlert(
                            MainDialogAlert(
                                message = BaseResText.underDevelopment,
                                cancelOperationText = BaseResText.cancelBtn
                            )
                        )
                    }
                    .padding(15.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(32.dp),
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_phone),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        NotificationManager.createDialogAlert(
                            MainDialogAlert(
                                message = BaseResText.underDevelopment,
                                cancelOperationText = BaseResText.cancelBtn
                            )
                        )
                    }
                    .padding(15.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(32.dp),
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_settings),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        NotificationManager.createDialogAlert(
                            MainDialogAlert(
                                message = BaseResText.underDevelopment,
                                cancelOperationText = BaseResText.cancelBtn
                            )
                        )
                    }
                    .padding(15.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(32.dp),
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_store),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        NotificationManager.createDialogAlert(
                            MainDialogAlert(
                                message = BaseResText.underDevelopment,
                                cancelOperationText = BaseResText.cancelBtn
                            )
                        )
                    }
                    .padding(15.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(32.dp),
            )
            Icon(
                imageVector = vectorResource(Res.drawable.input_users_three),
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        NotificationManager.createDialogAlert(
                            MainDialogAlert(
                                message = BaseResText.underDevelopment,
                                cancelOperationText = BaseResText.cancelBtn
                            )
                        )
                    }
                    .padding(15.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .size(32.dp),
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
            )
        }
    }
}


val KeyboardShownKey = SemanticsPropertyKey<Boolean>("KeyboardShownKey")
var SemanticsPropertyReceiver.keyboardShownProperty by KeyboardShownKey


@OptIn(ExperimentalLayoutApi::class)
@ExperimentalFoundationApi
@Composable
private fun UserInputText(
    keyboardType: KeyboardType = KeyboardType.Text,
    onTextChanged: (String) -> Unit,
    onSelectorChange: (InputSelector) -> Unit,
    textFieldValue: String,
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
                            message = BaseResText.underDevelopment,
                            cancelOperationText = BaseResText.cancelBtn
                        )
                    )
                }
                .size(28.dp),
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
            imageVector = vectorResource(Res.drawable.input_map_pin),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clip(RoundedCornerShape(5.dp))
                .clickable {
                    onSelectorChange(InputSelector.EMOJI)
                }
                .size(28.dp),
        )

        if (sendMessageEnabled) {
            Icon(
                imageVector = vectorResource(Res.drawable.input_send),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .clickable {
                        onMessageSent(textFieldValue)
                    }
                    .size(28.dp),
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
            )
        }

    }
}

@Composable
private fun BoxScope.UserInputTextField(
    textFieldValue: String,
    onTextChanged: (String) -> Unit,
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
            .background(MaterialTheme.colorScheme.surfaceVariant)
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
            if (textFieldValue.isNotBlank()) onMessageSent(textFieldValue)
        },
        maxLines = 1,
        //todo for show image in extField decorationBox = {innerTextField -> innerTextField()}
        //https://stackoverflow.com/questions/78145214/android-compose-textfield-with-image-using-inlinetextcontent
        //这个方法还是不好用，两个问题，第一个问题光标位置，如果我们使用私域emoji图标去处理，那么占位很小和实际图片占位不符，
        //第二个问题，我们使用Box去框住渲染的Text和本来的innerTextField，会同时出现两段文字，这个虽然可以用透明色解决，
        //但是还是需要对齐两段文字。主要还是第一个问题，光标位置处理不了，除非手写innerTextField的各种处理
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EmojiSelector(
    onTextAdded: (String) -> Unit,
    focusRequester: FocusRequester,
    sendNow: (String) -> Unit,
) {
    val mainModel: MainScreenModel = koinInject()
    val chatModel: ChatScreenModel = koinInject()
    val globalDataModel: GlobalDataModel = koinInject()
    val emojiProList = mainModel.getUserExData().emojiProListFlow.collectAsState().value
    val selectorExpandedCoroutine = rememberCoroutineScope()
    val tabPageState = rememberPagerState { InputEmojiTabModel.entries.size }
    val tabOrdinal = remember { derivedStateOf { tabPageState.currentPage } }
    val userState = mainModel.userState.collectAsState().value

    Column(
        modifier = Modifier
            .height(300.dp)
            .focusRequester(focusRequester)
            .focusTarget()
    ) {

        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = tabOrdinal.value,
            divider = {},
        ) {
            for (tabEnum in InputEmojiTabModel.entries) {
                Tab(
                    selected = tabOrdinal.value == tabEnum.ordinal,
                    onClick = {
                        selectorExpandedCoroutine.launch {
                            tabPageState.animateScrollToPage(
                                page = tabEnum.ordinal,
                                animationSpec = tween(durationMillis = 500)
                            )
                        }
                    },
                    text = {
                        Text(
                            text = stringResource(tabEnum.text),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    modifier = Modifier.clip(
                        RoundedCornerShape(10.dp)
                    ),
                )
            }
        }

        HorizontalPager(
            state = tabPageState,
            verticalAlignment = Alignment.Top
        ) { page ->

            val scrollState = rememberScrollState()

            Column(Modifier.fillMaxSize().verticalScroll(scrollState).padding(bottom = 10.dp)) {
                when (page) {
                    InputEmojiTabModel.EMOJI.ordinal -> {
                        val recentEmojiList = chatModel.recentEmojiList.collectAsState().value
                        if (recentEmojiList.isNotEmpty()) {
                            Text(
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    end = 8.dp,
                                    top = 10.dp,
                                    bottom = 0.dp
                                ),
                                text = stringResource(Res.string.main_chat_emoji_recently)
                            )
                            FlowRow(
                                modifier = Modifier.padding(5.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                            ) {
                                for (emoji in recentEmojiList) {
                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 7.dp,
                                            vertical = 5.dp
                                        )
                                            .clickable {
                                                onTextAdded(emoji)
                                                chatModel.addRecentEmojiList(emoji)
                                            },
                                        style = MaterialTheme.typography.headlineSmall,
                                        text = emoji,
                                    )
                                }
                            }
                            Text(
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    end = 8.dp,
                                    top = 0.dp,
                                    bottom = 0.dp
                                ),
                                text = stringResource(Res.string.main_chat_emoji_all)
                            )
                        }
                        FlowRow(
                            modifier = Modifier.padding(5.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            for (emoji in emojiList) {
                                Text(
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                                        .clickable {
                                            onTextAdded(emoji)
                                            chatModel.addRecentEmojiList(emoji)
                                        },
                                    style = MaterialTheme.typography.headlineSmall,
                                    text = emoji,
                                )
                            }
                        }
                    }

                    InputEmojiTabModel.KAOMOJI.ordinal -> {
                        val recentEmojiList = chatModel.recentKaomojiList.collectAsState().value
                        if (recentEmojiList.isNotEmpty()) {
                            Text(
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    end = 8.dp,
                                    top = 10.dp,
                                    bottom = 0.dp
                                ),
                                text = stringResource(Res.string.main_chat_emoji_recently)
                            )
                            FlowRow(
                                modifier = Modifier.padding(5.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                            ) {
                                for (emoji in recentEmojiList) {
                                    Text(
                                        modifier = Modifier.padding(
                                            start = 15.dp,
                                            end = 5.dp,
                                            top = 5.dp,
                                            bottom = 5.dp
                                        )
                                            .clickable {
                                                onTextAdded(emoji)
                                                chatModel.addRecentKaomojiList(emoji)
                                            },
                                        style = MaterialTheme.typography.bodyLarge,
                                        text = emoji,
                                    )
                                }
                            }
                            Text(
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    end = 8.dp,
                                    top = 0.dp,
                                    bottom = 0.dp
                                ),
                                text = stringResource(Res.string.main_chat_emoji_all)
                            )
                        }
                        FlowRow(
                            modifier = Modifier.padding(5.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            for (emoji in kaomojiList) {
                                Text(
                                    modifier = Modifier.padding(
                                        start = 15.dp,
                                        end = 5.dp,
                                        top = 5.dp,
                                        bottom = 5.dp
                                    )
                                        .clickable {
                                            onTextAdded(emoji)
                                            chatModel.addRecentKaomojiList(emoji)
                                        },
                                    style = MaterialTheme.typography.bodyLarge,
                                    text = emoji,
                                )
                            }
                        }
                    }


                    InputEmojiTabModel.EMOJI_PRO.ordinal -> {
                        val recentEmojiList = chatModel.recentEmojiProList.collectAsState().value
                        if (recentEmojiList.isNotEmpty()) {
                            Text(
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    end = 8.dp,
                                    top = 10.dp,
                                    bottom = 0.dp
                                ),
                                text = stringResource(Res.string.main_chat_emoji_recently)
                            )
                            FlowRow(
                                modifier = Modifier.padding(5.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                            ) {
                                for (emoji in recentEmojiList) {
                                    emojiProImage(
                                        emoji,
                                        "",
                                        sendNow,
                                        chatModel,
                                        userState,
                                        selectorExpandedCoroutine,
                                        globalDataModel
                                    )
                                }
                            }
                            Text(
                                modifier = Modifier.padding(
                                    start = 8.dp,
                                    end = 8.dp,
                                    top = 0.dp,
                                    bottom = 0.dp
                                ),
                                text = stringResource(Res.string.main_chat_emoji_all)
                            )
                        }
                        FlowRow(
                            modifier = Modifier.padding(5.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            for (emoji in emojiProList) {
                                emojiProImage(
                                    emoji.readAddress,
                                    emoji.id,
                                    sendNow,
                                    chatModel,
                                    userState,
                                    selectorExpandedCoroutine,
                                    globalDataModel
                                )
                            }

                            val launcher = rememberFilePickerLauncher(
                                type = FileKitType.File(
                                    extensions = listOf("webp", "png", "jpg", "jpeg", "gif")
                                ),
                            ) { file ->

                                if (file != null) {
                                    if (file.size() > 20 * 1024) {
                                        NotificationManager.createDialogAlert(
                                            MainDialogAlert(
                                                message = BaseResText.errorTooLargeUpload20,
                                                cancelOperationText = BaseResText.cancelBtn
                                            )
                                        )
                                    } else {
                                        selectorExpandedCoroutine.launch {
                                            val ret = BaseApi().uploadUserFile(
                                                token = mainModel.userState.value.token,
                                                1,
                                                file.readBytes(),
                                                file.name
                                            )
                                            if (ret.isNotEmpty()) {
                                                globalDataModel.clearLocalUserExData();
                                                val userEmojis = BaseApi().getStarEmojis(
                                                    mainModel.userState.value.token,
                                                )
                                                globalDataModel.resetUserEmoji(userEmojis)
                                            }
                                        }
                                    }
                                }
                            }

                            Icon(
                                imageVector = vectorResource(Res.drawable.input_upload),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .clip(RoundedCornerShape(5.dp))
                                    .clickable {
                                        launcher.launch()
                                    }
                                    .height(70.dp)
                                    .width(70.dp)
                                    .padding(15.dp),
                            )
                        }

                    }

                }
            }
        }


    }
}

@Composable
private fun emojiProImage(
    emojiAddress: String,
    emojiId: String,
    sendNow: (String) -> Unit,
    chatModel: ChatScreenModel,
    userState: UserState,
    selectorExpandedCoroutine: CoroutineScope,
    globalDataModel: GlobalDataModel
) {
    AsyncImage(
        request = ComposableImageRequest(uri = emojiAddress) {
            size(5000, 5000)
            precision(Precision.LESS_PIXELS)
            scale(Scale.CENTER_CROP)
        },
        contentDescription = null,
        modifier = Modifier.padding(4.dp).width(70.dp).height(70.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { _ ->
                        sendNow(emojiAddress)
                        chatModel.addRecentEmojiProList(emojiAddress)
                    },
                    onLongPress = { _ ->
                        if (emojiId.isNotBlank()) {
                            if (userState.token.isBlank()) {
                                MainDialogAlert(
                                    message = BaseResText.userNoLogin,
                                    cancelOperationText = BaseResText.cancelBtn
                                )
                            } else {
                                NotificationManager.createDialogAlert(
                                    MainDialogAlert(
                                        message = BaseResText.deleteImageProConfirm,
                                        confirmOperationText = BaseResText.confirmBtn,
                                        cancelOperationText = BaseResText.cancelBtn,
                                        confirmOperation = {
                                            selectorExpandedCoroutine.launch {
                                                BaseApi().unstarEmoji(
                                                    userState.token,
                                                    emojiId
                                                )

                                                globalDataModel.clearLocalUserExData();
                                                val userEmojis =
                                                    BaseApi().getStarEmojis(
                                                        userState.token
                                                    )
                                                globalDataModel.resetUserEmoji(
                                                    userEmojis
                                                )

                                                NotificationManager.removeDialogAlert()
                                            }
                                        }
                                    )
                                )
                            }
                        }
                    }
                )
            }
    )
}
