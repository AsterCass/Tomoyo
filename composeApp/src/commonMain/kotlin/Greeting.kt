import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlin.random.Random

class Greeting {
    private val platform = getPlatform()

    private val client = HttpClient()

//    private val rocketComponent = RocketComponent()

//    private val _greetingList = MutableStateFlow<List<String>>(listOf())
//    val greetingList: StateFlow<List<String>> get() = _greetingList

    fun greet(): List<String> = buildList {
        add(Random.nextInt().toString())
        add(if (Random.nextBoolean()) "Hi!" else "Hello!")
        add("Guess what this is! > ${platform.name.reversed()}!")
        add(daysPhrase())
    }

//    fun greet(): Flow<String> = flow {
//        emit(if (Random.nextBoolean()) "Hi!" else "Hello!")
//        delay(1.seconds)
//        emit("Guess what this is! > ${platform.name.reversed()}")
//        delay(1.seconds)
//        emit(daysPhrase())
//        emit(rocketComponent.launchPhrase())
//    }

    suspend fun greeting(): String {
//        delay(10.seconds)
        val response = client.get("https://api.astercasc.com/yui/asGo/health")
        return response.bodyAsText()
    }

}