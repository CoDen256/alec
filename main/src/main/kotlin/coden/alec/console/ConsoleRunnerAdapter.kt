package coden.alec.console

import coden.alec.app.AppRunner
import coden.console.ConsoleRunner

class ConsoleRunnerAdapter(private val consoleRunner: ConsoleRunner): AppRunner {
    override fun run() {
        consoleRunner.run()
    }
}