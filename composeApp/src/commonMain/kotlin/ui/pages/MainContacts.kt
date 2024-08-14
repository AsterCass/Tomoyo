package ui.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import constant.enums.RoleTypeEnum
import data.PublicUserSimpleModel
import data.model.ContactScreenModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import theme.subTextColor
import tomoyo.composeapp.generated.resources.Res
import tomoyo.composeapp.generated.resources.user_no_motto


@Composable
fun MainContactsScreen(
    screenModel: ContactScreenModel = koinInject(),
) {

    //coroutine
    val contactApiCoroutine = rememberCoroutineScope()

    //data
    val loadAllPublicUser = screenModel.loadAllPublicUser.collectAsState().value
    val publicUserDataList = screenModel.publicUserDataList.collectAsState().value

    //scroller
    val listState = rememberLazyListState()


    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        LazyColumn(state = listState) {

            items(publicUserDataList.size) { index ->
                PublicUserListItem(item = publicUserDataList[index])
            }


            item {
                if (!loadAllPublicUser) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                contactApiCoroutine.launch {
                    screenModel.loadPublicUser()
                }
            }


        }

    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PublicUserListItem(
    item: PublicUserSimpleModel,
) {

    val thisRoleType = RoleTypeEnum.getEnumByCode(item.roleType)
    //val thisGender = GenderTypeEnum.getEnumByCode(item.gender)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable {}
            .fillMaxHeight()
            .padding(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = item.avatar,
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.CenterVertically)
                .clip(CircleShape)
                .border(
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground),
                    shape = CircleShape
                ),
        )


        Column(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterVertically)
                .padding(start = 10.dp)
                .height(60.dp),
            verticalArrangement = Arrangement.Center
        ) {
            FlowRow {
                Text(
                    modifier = Modifier.padding(horizontal = 2.dp),
                    text = item.nickName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
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
//                Box(
//                    modifier = Modifier
//                        .padding(vertical = 2.dp, horizontal = 4.dp)
//                        .clip(
//                            RoundedCornerShape(3.dp)
//                        )
//                        .background(thisGender.color)
//                        .padding(horizontal = 2.dp)
//                ) {
//                    Text(
//                        modifier = Modifier.padding(horizontal = 2.dp),
//                        text = stringResource(thisGender.label),
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.background,
//                    )
//                }
            }

            Text(
                modifier = Modifier.padding(start = 3.dp, bottom = 3.dp),
                text = item.motto ?: stringResource(Res.string.user_no_motto),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.subTextColor,
            )
        }

    }

}
