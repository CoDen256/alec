package coden.alec.main.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("bot")
class AlecBotProperties {
    lateinit var token: String
}