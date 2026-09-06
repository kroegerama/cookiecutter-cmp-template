package {{ cookiecutter.namespace }}

import com.kroegerama.kmp.kaiteki.PlatformContext
import okio.Path
import okio.Path.Companion.toOkioPath
import java.io.File

actual fun imageCacheDirectory(context: PlatformContext): Path {
    val tmpDir = File(System.getProperty("java.io.tmpdir"))
    return tmpDir.resolve("{{ cookiecutter.application_id }}/image_cache").toOkioPath()
}
