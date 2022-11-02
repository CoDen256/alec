package coden.console.read

interface CommandReader {
    fun readNextCommand(): String?
}