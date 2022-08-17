package entities

import java.time.Instant

data class Rating (
    val id: String,
    val raterId: String,
    val factorId: String,
    val timestamp: Instant,
    val value: Long
)


interface RatingGateway{
    fun getRatings(): List<Rating>
    fun addRating(rating: Rating)
    fun updateRating(rating: Rating)
    fun deleteRating(ratingId: String)
}