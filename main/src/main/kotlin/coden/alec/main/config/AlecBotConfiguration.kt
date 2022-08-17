package coden.alec.main.config

import coden.alec.bot.AlecBot
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(AlecBotProperties::class)
class AlecBotConfiguration {


    @Bean
    fun alecBot(properties: AlecBotProperties): AlecBot{
        return AlecBot(properties.token).also {
            it.launch()
        }
    }

}