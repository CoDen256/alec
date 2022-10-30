package coden.console.read

import coden.console.dispatcher.CommandRequest

interface CommandReader {
    fun read(): CommandRequest?
}