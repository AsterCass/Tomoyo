package com.aster.yuno.tomoyo

import MainApp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import biz.PlayerHolder
import constant.enums.MainNavigationEnum
import data.PlatformInitData
import di.KoinInit
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import ui.components.clearAppNotification
import ui.components.createAppNotificationChannel


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()

        super.onCreate(savedInstanceState)

        PlayerHolder.init(applicationContext)
        
        if (GlobalContext.getOrNull() == null) {
            KoinInit().init {
                androidLogger()
                androidContext(applicationContext)
                modules()
            }
        }

        createAppNotificationChannel(this)

        clearAppNotification()

        setContent {
            MainApp(
                platformData = PlatformInitData(
                    extraNavigationList = listOf(
                        MainNavigationEnum.ARTICLES,
                        MainNavigationEnum.Contacts,
                        MainNavigationEnum.MUSICS,
                        MainNavigationEnum.SETTING,
                    )
                )
            )

        }
    }

    override fun onResume() {
        super.onResume()
        clearAppNotification()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    MainApp()
}