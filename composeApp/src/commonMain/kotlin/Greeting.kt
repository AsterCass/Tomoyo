import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class Greeting {
    private val platform = getPlatform()

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

}