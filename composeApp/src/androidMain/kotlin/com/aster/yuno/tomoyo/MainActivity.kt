package com.aster.yuno.tomoyo

import MainApp
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import constant.enums.MainNavigationEnum
import data.PlatformInitData
import di.KoinInit
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger


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
        super.onCreate(savedInstanceState)

        KoinInit().init {
            androidLogger()
            androidContext(thisContext)
            modules()
        }

        println("reload onCreate")
        setContent {
            println("reload setContent")
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