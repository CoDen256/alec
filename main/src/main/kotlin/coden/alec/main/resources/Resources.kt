package coden.alec.main.resources

import coden.alec.app.resources.MessageResource
import coden.alec.console.ConsoleCommandNamesResource
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("messages")
class Messages: MessageResource()

@ConfigurationProperties("commands")
class CommandNames: ConsoleCommandNamesResource()