package coden.alec.main.base

import coden.alec.app.config.scales.BaseScaleParser
import coden.alec.app.config.scales.BaseScaleResponder
import coden.alec.app.config.scales.BaseHelpActuator
import coden.alec.app.config.scales.BaseScaleActuator
import coden.alec.app.actuators.*
import coden.alec.app.display.*
import coden.alec.core.ScaleUseCaseFactory
import coden.alec.data.Scale
import coden.alec.main.resources.Messages
import coden.display.displays.MessageDisplay
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(Messages::class)
class ActuatorConfiguration {

    private val formatter = object : ScaleFormatter {
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
        messageDisplay: MessageDisplay,
        messages: Messages
    ): HelpActuator {
        return BaseHelpActuator(
            messageDisplay,
            messages
        )
    }

    @Bean
    fun scaleResponder(
        messageDisplay: MessageDisplay,
        messages: Messages
    ): ScaleResponder {
        return BaseScaleResponder(
            messageDisplay, messages, formatter
        )
    }

    @Bean
    fun scaleParser(): ScaleParser {
        return BaseScaleParser()
    }

    @Bean
    fun scaleActuator(
        useCaseFactory: ScaleUseCaseFactory,
        scaleResponder: ScaleResponder,
        scaleParser: ScaleParser
    ): ScaleActuator {
        return BaseScaleActuator(
            useCaseFactory,
            scaleResponder,
            scaleParser
        )
    }

}