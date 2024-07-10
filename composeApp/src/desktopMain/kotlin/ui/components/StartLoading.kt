package ui.components

import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage

class StartLoading : Application() {
    override fun start(stage: Stage) {
        println("加载开始动画1")
        val root = Group()
        val scene = Scene(root, 200.0, 150.0)
        scene.fill = Color.WHITE
        val text = Text(10.0, 90.0, "这里是开始动画")
        text.fill = Color.BROWN
        val font = Font(20.0)
        text.font = font
        root.children.add(text)
        stage.scene = scene
        stage.show()
        println("加载开始动画2")
    }

}