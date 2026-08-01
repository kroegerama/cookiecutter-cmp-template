package {{ cookiecutter.namespace }}

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() {
    Init.initAll(true)
    System.setProperty("apple.awt.application.appearance", "system")

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "{{ cookiecutter.app_name }}",
        ) {
            App()
        }
    }
}
