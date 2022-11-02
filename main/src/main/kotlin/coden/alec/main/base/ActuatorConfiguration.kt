package coden.alec.main.base

import coden.alec.app.actuators.BaseHelpActuator
import coden.alec.app.actuators.BaseScaleActuator
import coden.alec.app.actuators.HelpActuator
import coden.alec.app.actuators.ScaleActuator
import coden.alec.core.UseCaseFactory
import coden.alec.main.resources.Messages
import coden.display.displays.MessageDisplay
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(Messages::class)
class ActuatorConfiguration {

    @Bean
    fun helpActuator(
        useCaseFactory: UseCaseFactory,
        messageDisplay: MessageDisplay,
        messages: Messages
    ): HelpActuator {
        return BaseHelpActuator(
            useCaseFactory,
            messageDisplay,
            messages
        )
    }


    @Bean
    fun scaleActuator(
        useCaseFactory: UseCaseFactory,
        messageDisplay: MessageDisplay,
        messages: Messages
    ): ScaleActuator {
        return BaseScaleActuator(
            useCaseFactory,
            messageDisplay,
            messages
        )
    }

}