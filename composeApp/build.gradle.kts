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

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

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
            implementation(libs.kotlinx.coroutines.android)


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


            //https://github.com/mahozad/wavy-slider
            implementation("ir.mahozad.multiplatform:wavy-slider:2.0.0-alpha")


//            implementation("org.hildan.krossbow:krossbow-stomp-core:5.12.0")
//            implementation("org.hildan.krossbow:krossbow-websocket-sockjs:5.12.0")
//            runtimeOnly("org.glassfish.tyrus.bundles:tyrus-standalone-client:2.0.7")
//            runtimeOnly("com.fasterxml.jackson.core:jackson-databind:2.15.4")


            implementation("org.hildan.krossbow:krossbow-stomp-core:4.5.0")
            implementation("org.hildan.krossbow:krossbow-websocket-sockjs:4.5.0")
            implementation("org.glassfish.tyrus.bundles:tyrus-standalone-client:1.19")
            implementation("com.fasterxml.jackson.core:jackson-databind:2.13.5")


            implementation("io.coil-kt.coil3:coil-compose:3.0.0-alpha06")

            //https://github.com/DevSrSouza/compose-icons
            //implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.0")

//            implementation("media.kamel:kamel-image:0.9.5")


//            runtimeOnly("org.springframework.boot:spring-boot-starter-websocket:3.2.6")
//            implementation("org.apache.tomcat.embed:tomcat-embed-websocket:10.1.26")


//            implementation(libs.fastjson2)
//            runtimeOnly(libs.kotlin.reflect)
//            api(libs.koin.core)
//            implementation(libs.koin.compose.multiplatform)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.okhttp)

            val fxSuffix = "win"
            implementation("org.openjfx:javafx-media:21.0.3:$fxSuffix")
            implementation("org.openjfx:javafx-base:21.0.3:$fxSuffix")
            implementation("org.openjfx:javafx-swing:21.0.3:$fxSuffix")
            implementation("org.openjfx:javafx-graphics:21.0.3:$fxSuffix")
//            implementation("org.openjfx:javafx-controls:21.0.3:$fxSuffix")
//            implementation("org.openjfx:javafx-fxml:22.0.1:$fxSuffix")
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
            packageVersion = "1.1.4"

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


