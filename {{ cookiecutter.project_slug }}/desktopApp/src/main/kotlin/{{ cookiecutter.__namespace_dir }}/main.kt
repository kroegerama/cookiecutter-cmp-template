package {{ cookiecutter.namespace }}

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kroegerama.kmp.kaiteki.PlatformContext

fun main() {
    PlatformContext.initialize("{{ cookiecutter.application_id }}")
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
