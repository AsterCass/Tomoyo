package ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import biz.getChineseZodiac
import biz.getZodiac
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.github.panpf.sketch.AsyncImage
import com.github.panpf.sketch.LocalPlatformContext
import com.github.panpf.sketch.request.ImageRequest
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.brands.Github
import compose.icons.fontawesomeicons.brands.Qq
import compose.icons.fontawesomeicons.brands.Weixin
import compose.icons.fontawesomeicons.solid.BirthdayCake
import compose.icons.fontawesomeicons.solid.Envelope
import compose.icons.fontawesomeicons.solid.Paw
import compose.icons.fontawesomeicons.solid.StarAndCrescent
import constant.BaseResText
import constant.enums.GenderTypeEnum
import constant.enums.RoleTypeEnum
import constant.enums.UserDetailTabScreenTabModel
import data.UserDetailModel
import data.model.ContactScreenModel
import data.model.MainScreenModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import theme.baseBackground
import theme.subTextColor
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.articles
import tomoyo.composeapp.generated.resources.bg3
import tomoyo.composeapp.generated.resources.under_development
import tomoyo.composeapp.generated.resources.user_chat_btn
import tomoyo.composeapp.generated.resources.user_follow_btn
import tomoyo.composeapp.generated.resources.user_followers
import tomoyo.composeapp.generated.resources.user_following
import tomoyo.composeapp.generated.resources.user_friends
import tomoyo.composeapp.generated.resources.user_no_friend
import tomoyo.composeapp.generated.resources.user_thoughts
import ui.components.MainDialogAlert
import ui.components.NotificationManager


class UserDetailScreen(
    private val userId: String,
) : Screen {

    override val key: ScreenKey = uniqueScreenKey


    @OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        //inject
        val mainModel: MainScreenModel = koinInject()
        val contactScreenModel: ContactScreenModel = koinInject()
        val configBlock: (ImageRequest.Builder.() -> Unit) = koinInject()
        val context = LocalPlatformContext.current
        val isMobile: Boolean = koinInject(qualifier = named("isMobile"))

        //coroutine
        val userDetailApiCoroutine = rememberCoroutineScope()

        //navigation
        mainModel.updateShowNavBar(false)
        val navigator = LocalNavigator.currentOrThrow
        val loadingScreen = mainModel.loadingScreen.collectAsState().value

        //data
        val userDetailData = contactScreenModel.userDetail.collectAsState().value

        //user data
        val userState = mainModel.userState.collectAsState().value
        val token = userState.token

        //this data
        val statusBarHigh = if (isMobile) 30.dp else 0.dp
        val thisRoleType = RoleTypeEnum.getEnumByCode(userDetailData.roleType)
        val thisZodiac = getZodiac(
            userDetailData.birth.monthNumber, userDetailData.birth.dayOfMonth
        )
        val thisChineseZodiac = getChineseZodiac(userDetailData.birth)
        val tabPageState = rememberPagerState { UserDetailTabScreenTabModel.entries.size }

        userDetailApiCoroutine.launch {
            contactScreenModel.updateUserDetail(userId)
        }

        if (userDetailData.id != userId) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp)
                )
            }
            return
        }


        AnimatedVisibility(
            visible = !loadingScreen,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(Res.drawable.bg3),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                )



                Column(
                    Modifier.padding(
                        start = 15.dp,
                        end = 15.dp,
                        top = statusBarHigh + 4.dp,
                        bottom = 4.dp
                    ).fillMaxSize()

                ) {

                    Row(
                        Modifier.height(50.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            shape = RoundedCornerShape(15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.baseBackground,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                            contentPadding = PaddingValues(0.dp),
                            onClick = { navigator.popUntilRoot() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize().padding(top = 20.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 50.dp, bottom = 20.dp)
                                .clip(
                                    RoundedCornerShape(16.dp)
                                )
                                .background(
                                    color = MaterialTheme.colorScheme.baseBackground
                                )
                                .padding(horizontal = 20.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().height(70.dp),
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    horizontalAlignment = Alignment.End
                                ) {

                                    if (userState.userData.id != userId) {
                                        Button(
                                            onClick = {
                                                if (token.isNotBlank()) {
                                                    navigator.push(UserChatScreen(userId, ""))
                                                } else {
                                                    NotificationManager.createDialogAlert(
                                                        //todo jump to login
                                                        MainDialogAlert(
                                                            message = BaseResText.userNoLogin,
                                                            cancelOperationText = BaseResText.cancelBtn
                                                        )
                                                    )
                                                }
                                            },
                                            shape = RoundedCornerShape(5.dp),
                                            contentPadding = PaddingValues(
                                                vertical = 3.dp,
                                                horizontal = 1.dp
                                            ),
                                            colors = ButtonDefaults.buttonColors().copy(
                                                containerColor = MaterialTheme.colorScheme.secondary
                                            ),
                                            modifier = Modifier
                                                .height(22.dp)
                                        ) {
                                            Text(
                                                text = stringResource(Res.string.user_chat_btn),
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }

                                        Button(
                                            onClick = {
                                                NotificationManager.createDialogAlert(
                                                    MainDialogAlert(
                                                        message = BaseResText.underDevelopment,
                                                        cancelOperationText = BaseResText.cancelBtn
                                                    )
                                                )
                                            },
                                            shape = RoundedCornerShape(5.dp),
                                            contentPadding = PaddingValues(
                                                vertical = 3.dp,
                                                horizontal = 1.dp
                                            ),
                                            colors = ButtonDefaults.buttonColors().copy(
                                                containerColor = MaterialTheme.colorScheme.primary
                                            ),
                                            modifier = Modifier
                                                .height(22.dp)
                                        ) {
                                            Text(
                                                text = stringResource(Res.string.user_follow_btn),
                                                style = MaterialTheme.typography.bodySmall,
                                            )
                                        }
                                    }

                                }

                                Text(
                                    text = userDetailData.nickName,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Row(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .clip(
                                            RoundedCornerShape(3.dp)
                                        )
                                        .background(thisRoleType.color)
                                        .padding(horizontal = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = vectorResource(thisRoleType.logo),
                                        contentDescription = null,
                                        modifier = Modifier.size(10.dp),
                                        tint = Color.White
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 2.dp),
                                        text = stringResource(thisRoleType.label),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.background,
                                    )
                                }

                                Row(
                                    modifier = Modifier.padding(vertical = 5.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {

                                    Icon(
                                        imageVector = FontAwesomeIcons.Solid.BirthdayCake,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp).size(15.dp),
                                        tint = Color(255, 193, 7),
                                    )


                                    Text(
                                        text = userDetailData.birth.toString(),
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    VerticalDivider(
                                        modifier = Modifier
                                            .padding(10.dp).height(8.dp)
                                    )

                                    Icon(
                                        imageVector = FontAwesomeIcons.Solid.StarAndCrescent,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp).size(15.dp),
                                        tint = Color(63, 81, 181),
                                    )

                                    Text(
                                        text = stringResource(thisZodiac.label)
                                                + thisZodiac.logo,
                                        style = MaterialTheme.typography.bodySmall
                                    )

                                    VerticalDivider(
                                        modifier = Modifier
                                            .padding(10.dp).height(8.dp)
                                    )

                                    Icon(
                                        imageVector = FontAwesomeIcons.Solid.Paw,
                                        contentDescription = null,
                                        modifier = Modifier.padding(end = 8.dp).size(15.dp),
                                        tint = Color(121, 85, 72),
                                    )

                                    Text(
                                        text = stringResource(thisChineseZodiac.label)
                                                + thisChineseZodiac.logo,
                                        style = MaterialTheme.typography.bodySmall
                                    )


                                }

                                FlowRow(
                                    modifier = Modifier.padding(vertical = 5.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                ) {

                                    Row(
                                        modifier = Modifier.height(28.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            imageVector = FontAwesomeIcons.Solid.Envelope,
                                            contentDescription = null,
                                            modifier = Modifier.padding(end = 8.dp).size(15.dp),
                                            tint = Color(255, 162, 55),
                                        )

                                        Text(
                                            text = userDetailData.mail,
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        VerticalDivider(
                                            modifier = Modifier
                                                .padding(10.dp).height(8.dp)
                                        )

                                    }



                                    Row(
                                        modifier = Modifier.height(28.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            imageVector = FontAwesomeIcons.Brands.Qq,
                                            contentDescription = null,
                                            modifier = Modifier.padding(end = 8.dp).size(15.dp),
                                            tint = Color(55, 160, 244),
                                        )

                                        Text(
                                            text = userDetailData.socialLink.qq ?: "None",
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        VerticalDivider(
                                            modifier = Modifier
                                                .padding(10.dp).height(8.dp)
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.height(28.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            imageVector = FontAwesomeIcons.Brands.Weixin,
                                            contentDescription = null,
                                            modifier = Modifier.padding(end = 8.dp).size(15.dp),
                                            tint = Color(94, 183, 97),
                                        )

                                        Text(
                                            text = userDetailData.socialLink.wechat ?: "None",
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        VerticalDivider(
                                            modifier = Modifier
                                                .padding(10.dp).height(8.dp)
                                        )
                                    }


                                    Row(
                                        modifier = Modifier.height(28.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            imageVector = FontAwesomeIcons.Brands.Github,
                                            contentDescription = null,
                                            modifier = Modifier.padding(end = 8.dp).size(15.dp),
                                            tint = Color(25, 25, 25),
                                        )

                                        Text(
                                            text = userDetailData.socialLink.github ?: "None",
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                    }


                                }


                                FlowRow(
                                    modifier = Modifier.padding(vertical = 5.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                ) {

                                    Row(
                                        modifier = Modifier.height(25.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {

                                        Text(
                                            text = stringResource(Res.string.user_following) + ": ",
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        Text(
                                            text = userDetailData.community.followNum.toString(),
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        VerticalDivider(
                                            modifier = Modifier.padding(10.dp).height(8.dp)
                                        )

                                    }



                                    Row(
                                        modifier = Modifier.height(25.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {

                                        Text(
                                            text = stringResource(Res.string.user_followers) + ": ",
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        Text(
                                            text = userDetailData.community.fansNum.toString(),
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        VerticalDivider(
                                            modifier = Modifier.padding(10.dp).height(8.dp)
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.height(25.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {

                                        Text(
                                            text = stringResource(Res.string.user_friends) + ": ",
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        Text(
                                            text = userDetailData.community.friendNum.toString(),
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        VerticalDivider(
                                            modifier = Modifier.padding(10.dp).height(8.dp)
                                        )
                                    }


                                    Row(
                                        modifier = Modifier.height(25.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {

                                        Text(
                                            text = stringResource(Res.string.articles) + ": ",
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        Text(
                                            text = userDetailData.articleNum?.toString() ?: "0",
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        VerticalDivider(
                                            modifier = Modifier.padding(10.dp).height(8.dp)
                                        )

                                    }

                                    Row(
                                        modifier = Modifier.height(25.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {

                                        Text(
                                            text = stringResource(Res.string.user_thoughts) + ": ",
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        Text(
                                            text = userDetailData.thoughtNum?.toString() ?: "0",
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                    }

                                }


                                UserDetailTabScreen(
                                    tabPageState = tabPageState,
                                    userDetailData = userDetailData,
                                    changeTab = { tabEnum ->
                                        contactScreenModel.updateUserDetailTab(tabEnum)
                                        userDetailApiCoroutine.launch {
                                            tabPageState.animateScrollToPage(
                                                page = tabEnum.ordinal,
                                                animationSpec = tween(durationMillis = 1000)
                                            )
                                        }
                                    },
                                    toOtherUserDetail = {
                                        navigator.push(UserDetailScreen(it))
                                    }
                                )

                            }

                        }


                        AsyncImage(
                            request = ImageRequest(
                                context = context,
                                uri = userDetailData.avatar,
                                configBlock = configBlock,
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(
                                    border = BorderStroke(
                                        2.dp,
                                        MaterialTheme.colorScheme.background
                                    ),
                                    shape = CircleShape
                                )

                        )

                    }

                }


            }

        }


    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun UserDetailTabScreen(
        tabPageState: PagerState,
        userDetailData: UserDetailModel,
        changeTab: (UserDetailTabScreenTabModel) -> Unit,
        toOtherUserDetail: (String) -> Unit,
    ) {

        val tabOrdinal = remember { derivedStateOf { tabPageState.currentPage } }
        val configBlock: (ImageRequest.Builder.() -> Unit) = koinInject()
        val context = LocalPlatformContext.current

        TabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = tabOrdinal.value,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            divider = {},
        ) {
            for (tabEnum in UserDetailTabScreenTabModel.entries) {
                Tab(
                    selected = tabOrdinal.value == tabEnum.ordinal,
                    onClick = {
                        changeTab(tabEnum)
                    },
                    text = {
                        Text(
                            text = stringResource(tabEnum.text),
                            color = if (tabOrdinal.value == tabEnum.ordinal)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onBackground,
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
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            Column(
                modifier = Modifier.padding(top = 10.dp)
            ) {
                when (page) {
                    UserDetailTabScreenTabModel.FRIENDS.ordinal -> {
                        if (userDetailData.friendList.isEmpty()) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = stringResource(Res.string.user_no_friend),
                                    color = MaterialTheme.colorScheme.subTextColor
                                )
                            }
                        } else {
                            Column(
                                Modifier.verticalScroll(rememberScrollState())
                            ) {
                                for (friend in userDetailData.friendList) {

                                    val thisRoleType = RoleTypeEnum.getEnumByCode(friend.roleType)
                                    val thisGender = GenderTypeEnum.getEnumByCode(friend.gender)

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable {
                                                toOtherUserDetail(friend.id)
                                            }
                                            .padding(5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        AsyncImage(
                                            request = ImageRequest(
                                                context = context,
                                                uri = friend.avatar,
                                                configBlock = configBlock,
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(40.dp)
                                                .align(Alignment.CenterVertically)
                                                .clip(CircleShape)
                                        )

                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalArrangement = Arrangement.SpaceEvenly,
                                        ) {

                                            Text(
                                                modifier = Modifier.padding(start = 4.dp),
                                                text = friend.nickName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.primary,
                                            )

                                            Row(
                                                modifier = Modifier.padding(start = 1.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier
                                                        .padding(vertical = 2.dp, horizontal = 4.dp)
                                                        .clip(
                                                            RoundedCornerShape(3.dp)
                                                        )
                                                        .background(thisRoleType.color)
                                                        .padding(horizontal = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = vectorResource(thisRoleType.logo),
                                                        contentDescription = null,
                                                        modifier = Modifier.size(10.dp),
                                                        tint = Color.White
                                                    )
                                                    Text(
                                                        modifier = Modifier.padding(start = 2.dp),
                                                        text = stringResource(thisRoleType.label),
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.background,
                                                    )
                                                }
                                                Box(
                                                    modifier = Modifier
                                                        .padding(vertical = 2.dp, horizontal = 4.dp)
                                                        .clip(
                                                            RoundedCornerShape(3.dp)
                                                        )
                                                        .background(thisGender.color)
                                                        .padding(horizontal = 2.dp)
                                                ) {
                                                    Text(
                                                        modifier = Modifier.padding(horizontal = 2.dp),
                                                        text = stringResource(thisGender.label),
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.background,
                                                    )
                                                }
                                            }

                                        }


                                    }
                                }
                            }
                        }

                    }

                    else -> {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(stringResource(Res.string.under_development))
                        }
                    }
                }
            }

        }
    }
}


