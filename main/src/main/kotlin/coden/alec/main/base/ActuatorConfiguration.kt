package coden.alec.main.base

import coden.alec.app.actuators.BaseHelpActuator
import coden.alec.app.actuators.BaseScaleActuator
import coden.alec.app.actuators.HelpActuator
import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.formatter.ListScalesResponseFormatter
import coden.alec.core.UseCaseFactory
import coden.alec.data.Scale
import coden.alec.main.resources.Messages
import coden.display.displays.MessageDisplay
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(Messages::class)
class ActuatorConfiguration {

    private val formatter = object : ListScalesResponseFormatter {
        override fun format(response: List<Scale>): String {
            return response.mapIndexed { index, scale ->
                "${index}.[${scale.id}] - ${scale.name}(${scale.unit}):\n" +
                        scale.divisions.joinToString("\n") {
                            "\t${it.value} - ${it.description}"
                        }
            }.joinToString("\n\n")
        }

    }

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
            messages,
            formatter
        )
    }

}