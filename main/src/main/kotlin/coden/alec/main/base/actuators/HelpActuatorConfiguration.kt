package coden.alec.main.base.actuators

import coden.alec.app.actuators.HelpActuator
import coden.alec.app.config.help.BaseHelpActuator
import coden.alec.main.resources.Messages
import coden.display.displays.MessageDisplay
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(Messages::class)
@Configuration
class HelpActuatorConfiguration {
    @Bean
    fun helpActuator(
        messageDisplay: MessageDisplay,
        messages: Messages
    ): HelpActuator {
        return BaseHelpActuator(
            messageDisplay,
            messages
        )
    }
}