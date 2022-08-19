package coden.alec.main

import coden.alec.app.states.*
import coden.alec.app.states.Entry.Companion.entry
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MainApplicationTests {

    @Autowired
    lateinit var stateExecutor: StateExecutor

    @Test
    fun contextLoads() {

        stateExecutor.submit(HelpCommand)
        stateExecutor.submit(HelpCommand)
        stateExecutor.submit(HelpCommand)

    }

}

