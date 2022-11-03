package coden.alec.main.console

import coden.alec.main.resources.CommandNames
import coden.console.AliasBasedCommandParser
import coden.console.AliasMapper
import coden.console.ConsoleCommandReader
import coden.console.read.CommandParser
import coden.console.read.CommandReader
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CommandNames::class)
class ConsoleCommandProcessorConfiguration {
    @Bean
    fun reader(): CommandReader{
        return ConsoleCommandReader()
    }

    @Bean
    fun aliasMapper(commandNames: CommandNames): AliasMapper{
        return object: AliasMapper {
            override fun canMap(input: String): Boolean {
                return true
            }

            override fun map(input: String): String {
                return when {
                    input.startsWith("~") -> input.replace("~", "/${commandNames.navCommand} ")
                    input.first().isDigit() -> "/${commandNames.navCommand} $input"
                    input.startsWith("!") -> input.replace("!", "/${commandNames.textCommand} ")
                    input.startsWith("/") -> input
                    else -> "/${commandNames.textCommand} $input"
                }
            }
        }
    }

    @Bean
    fun parser(aliasMappers: List<AliasMapper>): CommandParser{
        return AliasBasedCommandParser(aliasMappers)
    }
}