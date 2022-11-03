package coden.alec.main.bot

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("runner.bot")
class AlecBotProperties {
    lateinit var token: String
}