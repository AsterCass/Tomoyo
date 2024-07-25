package data

import constant.enums.MainNavigationEnum

data class PlatformInitData(
    val extraNavigationList: List<MainNavigationEnum> = emptyList(),
)

