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

        stateExecutor.submit(Help)
        stateExecutor.submit(Help)
        stateExecutor.submit(Help)
        stateExecutor.submit(ListScales)
        stateExecutor.submit(ListScales)
        stateExecutor.submit(ListScales)
        stateExecutor.submit(Help)
        stateExecutor.submit(CreateScale)
        stateExecutor.submit(CreateScale)
        stateExecutor.submit(CreateScale, "something")
        stateExecutor.submit(CreateScale, "hello\nname\n1-interesting")
        stateExecutor.submit(ListScales)

        stateExecutor.submit(CreateScaleNoArgs)
        stateExecutor.submit(Text, " asd asd")
        stateExecutor.submit(Text, "scale")
        stateExecutor.submit(Text, " asd asd")
        stateExecutor.submit(Text, "unit")
        stateExecutor.submit(Text, "hello")
        stateExecutor.submit(Text, "1-interesting\n2-somewhatinteresting")

        stateExecutor.submit(ListScales)

    }

}

