package coden.alec.main.annotations

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty

@ConditionalOnProperty(prefix = "runner", name = ["active"], havingValue = "console")
annotation class ConsoleEnabled

@ConditionalOnProperty(prefix = "runner", name = ["active"], havingValue = "bot")
annotation class BotEnabled