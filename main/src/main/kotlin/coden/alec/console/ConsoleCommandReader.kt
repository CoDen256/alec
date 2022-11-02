package coden.alec.console

import coden.console.read.CommandReader

class ConsoleCommandReader: CommandReader {
    override fun readNextCommand(): String? {
        return readlnOrNull()
    }
}