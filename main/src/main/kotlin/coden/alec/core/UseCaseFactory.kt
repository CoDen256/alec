package coden.alec.core

interface UseCaseFactory {

    fun listScales(): ListScalesActivator
    fun createScale(): CreateScaleActivator

}