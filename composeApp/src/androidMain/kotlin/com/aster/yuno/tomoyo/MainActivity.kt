package com.aster.yuno.tomoyo

import MainApp
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import constant.enums.MainNavigationEnum
import data.PlatformInitData

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("reload onCreate")
        setContent {
            println("reload setContent")
            MainApp(
                platformData = PlatformInitData(
                    extraNavigationList = listOf(
                        MainNavigationEnum.ARTICLES,
                        MainNavigationEnum.CHAT,
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