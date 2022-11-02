package coden.alec.main

import coden.alec.app.AppRunner
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async

@Configuration
class MainConfiguration {

//    @EventListener(ApplicationReadyEvent::class)
    @Async
    @Bean
    fun runner(@Qualifier("telegram") runner: AppRunner): AppRunner{
        runner.run()
        return runner
    }
}