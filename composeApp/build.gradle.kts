import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {

    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    //alias(libs.plugins.javaFx)

    kotlin("plugin.serialization") version "2.0.0"
}

kotlin {

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)


            implementation("androidx.media3:media3-exoplayer:1.1.0")
            implementation("androidx.media3:media3-exoplayer-dash:1.1.0")
            implementation("androidx.media3:media3-ui:1.1.0")

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)



            implementation(libs.kotlinx.serialization)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)

            implementation(libs.navigation.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)




//            implementation(libs.fastjson2)
//            runtimeOnly(libs.kotlin.reflect)
//            api(libs.koin.core)
//            implementation(libs.koin.compose.multiplatform)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.okhttp)
        }
    }
}

android {
    namespace = "com.aster.yuno.tomoyo"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.aster.yuno.tomoyo"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {


    application {

        //jvmArgs += listOf("-Djna.library.path=app/resources/vlc")

        mainClass = "MainKt"

        buildTypes.release.proguard {
            isEnabled.set(true)
            optimize.set(true)
            obfuscate.set(true)
        }

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            //targetFormats(TargetFormat.Exe)
            includeAllModules = true

            packageName = "Tomoyo"
            packageVersion = "1.0.25"

            description = "Aster Casc Yuno Door Multiplatform App"
            copyright = "astercasc.com. All rights reserved."
            vendor = "Aster Casc"

            //appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
            //fromFiles(project.fileTree("include/") { include("**/*.dll") })

            //appResourcesRootDir.set(project.layout.projectDirectory.dir("libs"))

            windows {
                console = true
                menuGroup = "Tomoyo"
                iconFile.set(project.file("src/desktopMain/resources/snow.ico"))
                upgradeUuid = "a33226c1-436e-44e4-9f4a-f9fbc6fc0dde"
            }


        }

    }
}


