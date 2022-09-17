package coden.alec.main

data class Menu (
    val description: String,
    val items: List<MenuItem>
    )

data class MenuItem (
    val name: String
    )