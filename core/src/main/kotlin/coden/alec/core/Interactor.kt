package coden.alec.core

interface Interactor {
    fun execute(request: Request): Response
}