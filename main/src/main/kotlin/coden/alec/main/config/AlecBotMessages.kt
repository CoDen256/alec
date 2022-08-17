package coden.alec.main.config

import coden.alec.bot.messages.MessageResource
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("messages")
class AlecBotMessages: MessageResource()