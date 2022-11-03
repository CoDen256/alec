package coden.alec.core

interface UseCaseFactory {

    fun listScales(): ListScalesInteractor
    fun createScale(): CreateScaleInteractor
    fun deleteScale(): DeleteScaleInteractor
    fun purgeScale(): PurgeScaleInteractor
    fun updateScale(): UpdateScaleInteractor
}