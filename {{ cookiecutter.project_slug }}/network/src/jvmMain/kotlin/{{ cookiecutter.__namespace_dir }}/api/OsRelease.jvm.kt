package {{ cookiecutter.namespace }}.api

internal actual val osRelease: String =
    "${System.getProperty("os.name")} ${System.getProperty("os.version")}"
