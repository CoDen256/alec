package coden.alec.main.base.actuators

import coden.alec.app.actuators.*
import coden.alec.app.actuators.scale.*
import coden.alec.app.config.scale.*
import coden.alec.core.ScaleUseCaseFactory
import coden.alec.main.resources.Messages
import coden.display.displays.MessageDisplay
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(Messages::class)
class ScaleActuatorConfiguration {

    @Bean
    fun scaleFormatter(): ScaleFormatter{
        return BaseScaleFormatter()
    }

    @Bean
    fun scaleResponder(
        messageDisplay: MessageDisplay,
        messages: Messages,
        formatter: ScaleFormatter
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
    fun scaleUseCaseInvoker(useCaseFactory: ScaleUseCaseFactory): ScaleUseCaseInvoker{
        return BaseScaleUseCaseInvoker(useCaseFactory)
    }

    @Bean
    fun scaleRequestBuilder(): CreateScaleRequestBuilder {
        return BaseCreateScaleRequestBuilder()
    }

    @Bean
    fun scaleActuator(
         scaleResponder: ScaleResponder,
         scaleParser: ScaleParser,
         scaleUseCaseInvoker: ScaleUseCaseInvoker,
         scaleRequestBuilder: CreateScaleRequestBuilder
    ): ScaleActuator {
        return BaseScaleActuator(scaleResponder, scaleParser, scaleRequestBuilder, scaleUseCaseInvoker)
    }

}