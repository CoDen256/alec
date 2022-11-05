package coden.alec.main.base

import coden.alec.core.*
import coden.alec.data.ScaleGateway
import coden.alec.interactors.definer.scale.BaseCreateScaleInteractor
import coden.alec.interactors.definer.scale.BaseListScalesInteractor
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
    fun scaleUseCaseFactory(scalesGateway: ScaleGateway): ScaleUseCaseFactory {
        return object : ScaleUseCaseFactory {
            override fun listScales(): coden.alec.core.ListScalesInteractor {
                return BaseListScalesInteractor(scalesGateway)
            }

            override fun createScale(): coden.alec.core.CreateScaleInteractor {
                return BaseCreateScaleInteractor(scalesGateway)
            }

            override fun deleteScale(): DeleteScaleInteractor {
                TODO("Not yet implemented")
            }

            override fun purgeScale(): PurgeScaleInteractor {
                TODO("Not yet implemented")
            }

            override fun updateScale(): UpdateScaleInteractor {
                TODO("Not yet implemented")
            }
        }
    }
}