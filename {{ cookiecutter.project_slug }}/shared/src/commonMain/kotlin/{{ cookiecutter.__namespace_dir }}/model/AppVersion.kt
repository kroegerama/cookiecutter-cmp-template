package {{ cookiecutter.namespace }}.model

data class AppVersion(
    val versionName: String,
    val versionCode: String,
    val applicationId: String,
)
