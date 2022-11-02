package coden.alec.main.config

import coden.alec.app.resources.MessageResource
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("messages")
class Messages: MessageResource()