package ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import constant.enums.RoleTypeEnum
import data.model.ContactScreenModel
import data.model.MainScreenModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import org.koin.core.qualifier.named
import theme.baseBackground
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.articles
import tomoyo.composeapp.generated.resources.bg3
import tomoyo.composeapp.generated.resources.user_chat_btn
import tomoyo.composeapp.generated.resources.user_follow_btn
import tomoyo.composeapp.generated.resources.user_followers
import tomoyo.composeapp.generated.resources.user_following
import tomoyo.composeapp.generated.resources.user_friends
import tomoyo.composeapp.generated.resources.user_thoughts


class UserDetailScreen(
    private val userId: String,
) : Screen {

    override val key: ScreenKey = uniqueScreenKey


    @OptIn(ExperimentalLayoutApi::class)
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
        val navigator = LocalNavigator.currentOrThrow
        val loadingScreen = mainModel.loadingScreen.collectAsState().value

        //data
        val userDetailData = contactScreenModel.userDetail.collectAsState().value

        //this data
        val statusBarHigh = if (isMobile) 30.dp else 0.dp
        val thisRoleType = RoleTypeEnum.getEnumByCode(userDetailData.roleType)
        val thisZodiac = getZodiac(
            userDetailData.birth.monthNumber, userDetailData.birth.dayOfMonth
        )
        val thisChineseZodiac = getChineseZodiac(userDetailData.birth)


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
                            colors = ButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
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

                                    Button(
                                        onClick = {},
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
                                        onClick = {},
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

                                Text(
                                    text = userDetailData.nickName,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Row(
                                    modifier = Modifier
                                        .padding(10.dp)
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
                                    modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
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
                                    modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                ) {

                                    Row(
                                        modifier = Modifier.height(30.dp),
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
                                        modifier = Modifier.height(30.dp),
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
                                        modifier = Modifier.height(30.dp),
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
                                        modifier = Modifier.height(30.dp),
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
                                    modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                ) {

                                    Row(
                                        modifier = Modifier.height(30.dp),
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
                                        modifier = Modifier.height(30.dp),
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
                                        modifier = Modifier.height(30.dp),
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
                                        modifier = Modifier.height(30.dp),
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
                                        modifier = Modifier.height(30.dp),
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
}