package coden.alec.app.actuators.scale

interface ScaleIdMapper {
    fun mapIdToRef(scaleId: String): String
    fun mapRefToId(scaleId: String): String
}