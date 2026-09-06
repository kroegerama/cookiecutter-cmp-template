package {{ cookiecutter.namespace }}

import com.kroegerama.kmp.kaiteki.PlatformContext
import okio.Path

expect fun imageCacheDirectory(context: PlatformContext): Path
