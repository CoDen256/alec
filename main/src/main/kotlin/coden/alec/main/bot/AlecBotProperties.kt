package coden.alec.main.bot

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("bot")
class AlecBotProperties {
    lateinit var token: String
}