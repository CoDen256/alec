package coden.alec.main.config

import coden.alec.bot.AlecBot
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(AlecBotProperties::class, AlecBotMessages::class)
class AlecBotConfiguration {



    @Bean
    fun alecBot(properties: AlecBotProperties, messages: AlecBotMessages): AlecBot{
        return AlecBot(properties.token, messages).also {
            it.launch()
        }
    }

}