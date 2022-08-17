package coden.alec.data

data class Rater(
    val id: String,
    val name: String,
    val username: String,
    val subscriptions: List<String>
)


interface RaterGateway{
    fun getRaters(): List<Rater>
    fun addRater(rater: Rater)
    fun updateRater(rater: Rater)
    fun deleteRater(raterId: String)
}