package coden.alec.main.bot

import coden.alec.main.resources.CommandNames
import coden.alec.main.resources.Messages
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(AlecBotProperties::class, Messages::class, CommandNames::class)
class AlecConfiguration {

}