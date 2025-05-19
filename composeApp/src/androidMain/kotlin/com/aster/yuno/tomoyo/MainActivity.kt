package com.aster.yuno.tomoyo

import MainApp
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import biz.getGoogleMessageToken
import constant.enums.MainNavigationEnum
import data.PlatformInitData
import di.KoinInit
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import ui.components.createAppNotificationChannel


class MainActivity : ComponentActivity() {

    private val thisContext: Context = this

    companion object {
        @SuppressLint("StaticFieldLeak")
        var mainContext: Context? = null
    }

    init {
        mainContext = thisContext
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        
        if (GlobalContext.getOrNull() == null) {
            KoinInit().init {
                androidLogger()
                androidContext(thisContext)
                modules()
            }
        }

        createAppNotificationChannel(this)
        getGoogleMessageToken();

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
}

@Preview
@Composable
fun AppAndroidPreview() {
    MainApp()
}