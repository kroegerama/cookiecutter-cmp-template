package {{ cookiecutter.namespace }}.api

import platform.UIKit.UIDevice

internal actual val osRelease: String = UIDevice.currentDevice.systemVersion
