package coden.alec.app.actuators.scale

interface ScaleIdMapper {
    fun mapIdToIndex(scaleId: String): String
    fun mapIndexToId(index: String): String
}