package coden.alec.console

import coden.alec.app.resources.CommandNamesResource

open class ConsoleCommandNamesResource : CommandNamesResource() {
    lateinit var textCommand: String
    lateinit var navCommand: String
}