package coden.alec.core

interface UseCaseFactory {

    fun listScales(): ListScalesActivator
    fun createScale(): CreateScaleActivator
    fun deleteScale(): DeleteScaleActivator
    fun purgeScale(): PurgeScaleActivator
    fun updateScale(): UpdateScaleActivator
}