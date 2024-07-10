import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {

    println("这里是开始动画")

//    thread {
    println("加载开始动画")
//    Application.launch(StartLoading::class.java)
//    }


    println("同步加载主应用")


    application {

        println("应用加载完成")
//        Platform.exit()

//    https://stackoverflow.com/questions/68246840/how-to-avoid-main-libvlc-error-when-using-the-python-vlc-package
//    "C:\Program Files\VideoLAN\VLC\vlc-cache-gen.exe" "C:\Program Files\VideoLAN\VLC\plugins"

//    val tempDir = Files.createTempDirectory("nativeLibs").toFile()
//    tempDir.deleteOnExit() // Ensure the temp directory is deleted on ex
//    try {
//        // Define the path to the native directory inside resources
//        val resourceDir = "/vlc"
//
//        // Get the list of native libraries in the directory
//        val nativeLibs: MutableList<String> = mutableListOf();
//
//        val resourceURL = this::class.java.getResource(resourceDir)
//            ?: throw RuntimeException("Resource directory $resourceDir not found")
//
//        if (resourceURL.protocol == "jar") {
//            // Running from a JAR file
//            val jarPath = resourceURL.path.substring(5, resourceURL.path.indexOf("!"))
//            val jarFile = JarFile(File(jarPath))
//            val entries = jarFile.entries()
//            while (entries.hasMoreElements()) {
//                val entry = entries.nextElement()
//                if (entry.name.startsWith(resourceDir.drop(1)) && !entry.isDirectory) {
//                    nativeLibs.add(entry.name)
//                }
//            }
//        } else {
//            // Running from the filesystem
//            val dir = File(resourceURL.toURI())
//            if (dir.isDirectory) {
//                dir.listFiles()?.forEach { file ->
//                    if (file.isFile) {
//                        nativeLibs.add(file.name)
//                    }
//                }
//            }
//        }
//
//        // Copy each native library to the temporary directory
//        nativeLibs.forEach { lib ->
//            val tempLib = File(tempDir, lib)
//            tempLib.parentFile.mkdirs()
//            tempLib.deleteOnExit() // Ensure the temp file is deleted on exit
//            this::class.java.getResourceAsStream("/$lib")?.use { inputStream ->
//                FileOutputStream(tempLib).use { fos ->
//                    val buffer = ByteArray(1024)
//                    var bytesRead: Int
//                    while ((inputStream.read(buffer).also { bytesRead = it }) != -1) {
//                        fos.write(buffer, 0, bytesRead)
//                    }
//                }
//            } ?: throw RuntimeException("Library $lib not found in resources")
//        }
//        // Your library loading code here
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//    // Set the system property to point to the temporary directory
//    System.setProperty("jna.library.path", tempDir.toString() + "/vlc")
//    // Now you can load your libraries using JNA
//    println("Native libraries loaded from: $tempDir")

        //   System.setProperty("jna.library.path", "Z:\\workspace\\yuno\\tomoyo\\composeApp\\libs\\common\\vlc")
        //System.setProperty("jna.library.path", "app/resources/vlc")

    Window(
        onCloseRequest = ::exitApplication,
        title = "Tomoyo",
        icon = MyAppIcon,

    ) {
        MainApp()
    }
    }
}

object MyAppIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color.Green, Offset(size.width / 4, 0f), Size(size.width / 2f, size.height))
        drawOval(Color.Blue, Offset(0f, size.height / 4), Size(size.width, size.height / 2f))
        drawOval(
            Color.Red,
            Offset(size.width / 4, size.height / 4),
            Size(size.width / 2f, size.height / 2f)
        )
    }
}



