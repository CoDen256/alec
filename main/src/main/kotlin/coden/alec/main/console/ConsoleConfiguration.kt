package coden.alec.main.console

import coden.alec.main.resources.CommandNames
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CommandNames::class)
class ConsoleConfiguration {



}