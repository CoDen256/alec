package entities

data class Configuration(
    val reminderCron: String,
    val requesterCron: String,
    val repeatIntervalHours: Long
)

interface ConfigurationGateway{
    fun getReminderCron(): String
    fun getRequesterCron(): String
    fun getRepeatIntervalHours(): Long
}