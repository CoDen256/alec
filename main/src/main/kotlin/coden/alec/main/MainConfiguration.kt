package coden.alec.main

import coden.alec.app.AppRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async

@Configuration
class MainConfiguration {

    @Async
    @Bean
    fun runner(runner: AppRunner): Any {
        runner.run()
        return runner
    }
}