//import io.ktor.client.*
//import io.ktor.client.plugins.contentnegotiation.*
//import io.ktor.serialization.kotlinx.json.*
//import kotlinx.serialization.json.Json
//import io.ktor.client.request.*
//import io.ktor.client.call.*
//import kotlinx.datetime.Instant
//import kotlinx.datetime.TimeZone
//import kotlinx.datetime.toLocalDateTime
//
//class RocketComponent {
//    private val httpClient = HttpClient {
//        install(ContentNegotiation) {
//            json(Json {
//                prettyPrint = true
//                isLenient = true
//                ignoreUnknownKeys = true
//            })
//        }
//    }
//
//    private suspend fun getDateOfLastSuccessfulLaunch(): String {
//        val rockets: List<RocketLaunch> = httpClient.get("https://api.spacexdata.com/v4/launches").body()
//        val lastSuccessLaunch = rockets.last { it.launchSuccess == true }
//        val date = Instant.parse(lastSuccessLaunch.launchDateUTC)
//            .toLocalDateTime(TimeZone.currentSystemDefault())
//
//        return "${date.month} ${date.dayOfMonth}, ${date.year}"
//    }
//
//    suspend fun launchPhrase(): String =
//        try {
//            "The last successful launch was on ${getDateOfLastSuccessfulLaunch()} 🚀"
//        } catch (e: Exception) {
//            println("Exception during getting the date of the last successful launch $e")
//            "Error occurred"
//        }
//
//
//
//}