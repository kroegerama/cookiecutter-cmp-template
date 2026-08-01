package {{ cookiecutter.namespace }}.api

import android.os.Build

internal actual val osRelease: String = Build.VERSION.RELEASE
