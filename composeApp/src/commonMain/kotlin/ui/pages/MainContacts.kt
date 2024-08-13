package ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.Heart
import data.PublicUserSimpleModel
import data.model.ContactScreenModel
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import theme.deepIconColor
import theme.subTextColor


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

@Composable
fun PublicUserListItem(
    item: PublicUserSimpleModel,
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable {}
            .padding(vertical = 10.dp)
            .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(0.55f)
                .align(Alignment.CenterVertically)
                .padding(start = 10.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 2.dp, bottom = 3.dp),
                text = item.nickName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                modifier = Modifier.padding(start = 3.dp, bottom = 3.dp),
                text = item.gender.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.subTextColor,
            )
        }

        Row(
            modifier = Modifier.weight(0.3f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = FontAwesomeIcons.Regular.Heart,
                contentDescription = null,
                modifier = Modifier
                    .padding(2.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {}
                    .size(22.dp),
                tint = MaterialTheme.colorScheme.deepIconColor
            )
        }

    }

}
