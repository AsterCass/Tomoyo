package data

import kotlinx.serialization.Serializable

@Serializable
class ResultObj<T>(
    var status: Int,
    var message: String? = null,
    var data: T? = null,
)



