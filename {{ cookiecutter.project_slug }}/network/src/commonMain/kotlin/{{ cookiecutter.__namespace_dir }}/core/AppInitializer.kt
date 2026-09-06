package {{ cookiecutter.namespace }}.core

interface AppInitializer {

    val order: Int get() = 0

    fun init()
}
