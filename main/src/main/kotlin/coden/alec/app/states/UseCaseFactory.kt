package coden.alec.app.states

import coden.alec.core.ListScalesActivator

interface UseCaseFactory {

    fun listScales(): ListScalesActivator

}