package coden.alec.main.base

import coden.alec.core.*
import coden.alec.data.ScaleGateway
import coden.alec.interactors.definer.scale.CreateScaleInteractor
import coden.alec.interactors.definer.scale.ListScalesInteractor
import gateway.memory.ScaleInMemoryGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UseCaseConfiguration {

    @Bean
    fun scalesGateway(): ScaleGateway{
        return ScaleInMemoryGateway()
    }

    @Bean
    fun useCaseFactory(scalesGateway: ScaleGateway): UseCaseFactory {
        return object : UseCaseFactory {
            override fun listScales(): ListScalesActivator {
                return ListScalesInteractor(scalesGateway)
            }

            override fun createScale(): CreateScaleActivator {
                return CreateScaleInteractor(scalesGateway)
            }

            override fun deleteScale(): DeleteScaleActivator {
                TODO("Not yet implemented")
            }

            override fun purgeScale(): PurgeScaleActivator {
                TODO("Not yet implemented")
            }

            override fun updateScale(): UpdateScaleActivator {
                TODO("Not yet implemented")
            }
        }
    }
}