object OS {
    val current: OSType
    val appdataPath: String

    const val appName = "BookTyper"

    init {
        val os = System.getProperty("os.name").toLowerCase()

        current = when {
            os.contains("win") -> OSType.WIN
            os.contains("mac") || os.contains("darwin") -> OSType.MAC
            os.contains("nux") || os.contains("nix") || os.contains("aix") -> OSType.LINUX
            else -> OSType.OTHER
        }

        appdataPath = when (current) {
            OSType.WIN -> System.getenv("APPDATA") + "\\$appName\\"
            OSType.MAC -> System.getProperty("user.home") + "/Library/Application Support/$appName/"
            OSType.LINUX -> System.getProperty("user.home") + "/.$appName/"
            OSType.OTHER -> System.getProperty("user.home") + "/.$appName/"
        }
    }
}

enum class OSType { LINUX, MAC, WIN, OTHER }