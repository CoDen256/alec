package coden.alec.main

import coden.alec.app.AppRunner
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async

@Configuration
class Configuration {

    @Async
    fun runner(@Qualifier("telegram") runner: AppRunner){
        runner.run()
    }
}