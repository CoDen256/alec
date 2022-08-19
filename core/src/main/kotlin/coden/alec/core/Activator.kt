package coden.alec.core

interface Activator {
    fun execute(request: Request): Response
}