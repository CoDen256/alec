package coden.alec.main

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class MainApplication
// TODO: Main Menu with requesting the scales and etc inlined in markup menu/keyboard
fun main(args: Array<String>) {
    runApplication<MainApplication>(*args)
}
