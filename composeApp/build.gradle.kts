import org.gradle.internal.os.OperatingSystem
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {

    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    //alias(libs.plugins.javaFx)

    kotlin("plugin.serialization") version "2.0.20"
}

val os: OperatingSystem = OperatingSystem.current()

val platform = when {
    os.isWindows -> "win"
    os.isMacOsX -> "mac"
    else -> "linux"
}

val jdkVersion = "17"

kotlin {

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
        freeCompilerArgs.addAll(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=" +
                    "${project.rootDir.absolutePath}/composeApp/config/compose_compiler_config.conf"
        )
    }

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.android)

            implementation(libs.koin.android)

            implementation("androidx.media3:media3-exoplayer:1.3.1")
            implementation("androidx.media3:media3-exoplayer-dash:1.3.1")
            implementation("androidx.media3:media3-ui:1.3.1")


            implementation("androidx.datastore:datastore-preferences:1.1.1")


            implementation("com.google.accompanist:accompanist-permissions:0.36.0")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)



            implementation(libs.kotlinx.serialization)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)

//            implementation(libs.navigation.compose)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.lifecycle.viewmodel.compose)


            //https://github.com/adrielcafe/voyager
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.tabNavigator)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)


            //https://github.com/InsertKoinIO/koin
            api(libs.koin.core)
            api(libs.koin.compose)

            //https://github.com/joffrey-bion/krossbow
            implementation("org.hildan.krossbow:krossbow-stomp-core:8.0.0")
            implementation("org.hildan.krossbow:krossbow-websocket-ktor:8.0.0")

            //https://github.com/panpf/sketch
            //https://github.com/panpf/sketch/blob/main/docs/wiki/animated_image.md
            implementation("io.github.panpf.sketch4:sketch-compose:4.0.0-alpha05")
            implementation("io.github.panpf.sketch4:sketch-animated:4.0.0-alpha05")

            //https://github.com/DevSrSouza/compose-icons
            implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.0")

            //https://github.com/russhwolf/multiplatform-settings
            implementation("com.russhwolf:multiplatform-settings:1.2.0")
            implementation("com.russhwolf:multiplatform-settings-datastore:1.2.0")
            implementation("com.russhwolf:multiplatform-settings-coroutines:1.2.0")

            //tools https://www.hutool.cn/ for chinese date
            implementation("cn.hutool:hutool-core:5.8.31")


            //https://github.com/MohamedRejeb/Compose-Rich-Editor/blob/main/docs/getting_started.md
            //https://github.com/MohamedRejeb/Compose-Rich-Editor/issues/306
            //还有很多东西不支持，先不做吧
            //implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc05-k2")

            //https://github.com/KevinnZou/compose-webview-multiplatform
            //高版本的桌面版需要下载，低版本的比如1.2使用javafx实现，但是桌面端会报错，原因没找到，
            //而且我也不是很想用嵌入html的方式来展示markdown文档
            //implementation("io.github.KevinnZou:compose-webview:0.33.3")

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.kotlinx.coroutines.swing)

            implementation("org.openjfx:javafx-base:$jdkVersion:${platform}")
            implementation("org.openjfx:javafx-graphics:$jdkVersion:${platform}")
            implementation("org.openjfx:javafx-media:$jdkVersion:${platform}")
            implementation("org.openjfx:javafx-swing:$jdkVersion:${platform}")
            //implementation("org.openjfx:javafx-web:$jdkVersion:${platform}")
            //implementation("org.openjfx:javafx-controls:innerJdkVersion:${platform}")
            //implementation("org.openjfx:javafx-fxml:innerJdkVersion:${platform}")
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
        minSdk = 26
    }
    packaging {
        resources {
            excludes += "/META-INF/**"
        }
    }
    buildTypes {

        release {
            setProguardFiles(listOf(File("proguard-rules.pro")))
            isMinifyEnabled = true
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


dependencies {
    debugImplementation(compose.uiTooling)
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
//            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            targetFormats(TargetFormat.Exe)
            includeAllModules = true

            packageName = "Tomoyo"
            packageVersion = "1.3.4"

            description = "Tomoyo"
            copyright = "astercasc.com. All rights reserved."
            vendor = "Aster Casc"

            //appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
            //fromFiles(project.fileTree("include/") { include("**/*.dll") })

            //appResourcesRootDir.set(project.layout.projectDirectory.dir("libs"))

            windows {
//                console = true
                menuGroup = "Tomoyo"
                iconFile.set(project.file("src/desktopMain/resources/logo_pro.ico"))
                upgradeUuid = "a33226c1-436e-44e4-9f4a-f9fbc6fc0dde"
            }


        }

    }
}


