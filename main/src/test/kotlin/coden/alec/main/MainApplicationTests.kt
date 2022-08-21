package coden.alec.main

import coden.alec.app.states.*
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
        stateExecutor.submit(ListScalesCommand)
        stateExecutor.submit(ListScalesCommand)
        stateExecutor.submit(ListScalesCommand)
        stateExecutor.submit(HelpCommand)
        stateExecutor.submit(CreateScaleCommand)
        stateExecutor.submit(CreateScaleCommand)
        stateExecutor.submit(CreateScaleCommand, "something")
        stateExecutor.submit(CreateScaleCommand, "hello\nname\n1-interesting")
        stateExecutor.submit(ListScalesCommand)

        stateExecutor.submit(CreateScaleNoArgs)
        stateExecutor.submit(TextCommand, " asd asd")
        stateExecutor.submit(TextCommand, "scale")
        stateExecutor.submit(TextCommand, " asd asd")
        stateExecutor.submit(TextCommand, "unit")
        stateExecutor.submit(TextCommand, "hello")
        stateExecutor.submit(TextCommand, "1-interesting\n2-somewhatinteresting")

        stateExecutor.submit(ListScalesCommand)

    }

}

