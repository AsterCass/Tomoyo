rootProject.name = "tomoyo"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")


pluginManagement {
    repositories {
//        maven(url = "https://mirrors.cloud.tencent.com/gradle/")

        maven(url = "https://maven.aliyun.com/repository/public/")
        maven(url = "https://maven.aliyun.com/repository/jcenter/")
        maven(url = "https://maven.aliyun.com/repository/google/")
        maven(url = "https://maven.aliyun.com/repository/gradle-plugin/")





        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://jogamp.org/deployment/maven")

//        maven(url = "https://mirrors.cloud.tencent.com/gradle/")

//        maven(url = "https://maven.aliyun.com/repository/public/")
//        maven(url = "https://maven.aliyun.com/repository/jcenter/")
//        maven(url = "https://maven.aliyun.com/repository/google/")
//        maven(url = "https://maven.aliyun.com/repository/gradle-plugin/")


        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")