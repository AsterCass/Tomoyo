package biz

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScreenTransition
import cafe.adriel.voyager.transitions.ScreenTransitionContent
import constant.enums.MainNavigationEnum
import constant.enums.ViewEnum

@Composable
fun TabTransition(
    secLastScreenKey: String,
    navigator: Navigator,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    ),
    content: ScreenTransitionContent = { it.Content() }
) {
    SlideTransition(
        secLastScreenKey = secLastScreenKey,
        navigator = navigator,
        modifier = modifier,
        animationSpec = animationSpec,
        disposeScreenAfterTransitionEnd = false,
        content = content
    )
}


@OptIn(ExperimentalVoyagerApi::class)
@Composable
private fun SlideTransition(
    secLastScreenKey: String,
    navigator: Navigator,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<IntOffset> = spring(
        stiffness = Spring.StiffnessMediumLow,
        visibilityThreshold = IntOffset.VisibilityThreshold
    ),
    disposeScreenAfterTransitionEnd: Boolean = false,
    content: ScreenTransitionContent = { it.Content() }
) {
    ScreenTransition(
        navigator = navigator,
        transition = {

            if (navigator.lastItem.key.startsWith(ViewEnum.TAB_MAIN_HOME.code) ||
                secLastScreenKey.startsWith(ViewEnum.TAB_MAIN_HOME.code)
            ) {
                fadeIn() togetherWith fadeOut()
            } else {
                val lastTabEnum = MainNavigationEnum.getEnumByCode(navigator.lastItem.key)
                val lastSecTabEnum = MainNavigationEnum.getEnumByCode(secLastScreenKey)


                val isToRight = lastTabEnum > lastSecTabEnum

                val (initialOffset, targetOffset) = if (isToRight)
                    ({ size: Int -> size }) to ({ size: Int -> -size })
                else ({ size: Int -> -size }) to ({ size: Int -> size })


                slideInHorizontally(animationSpec, initialOffset) togetherWith
                        slideOutHorizontally(animationSpec, targetOffset)
            }
        },
        modifier = modifier,
        disposeScreenAfterTransitionEnd = disposeScreenAfterTransitionEnd,
        content = content,
    )
}

