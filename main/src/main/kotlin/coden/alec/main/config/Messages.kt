package coden.alec.main.config

import coden.alec.app.messages.MessageResource
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("messages")
class Messages: MessageResource()