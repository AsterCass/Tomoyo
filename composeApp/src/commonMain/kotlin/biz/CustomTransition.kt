package biz

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import cafe.adriel.voyager.core.annotation.ExperimentalVoyagerApi
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScreenTransition
import cafe.adriel.voyager.transitions.ScreenTransitionContent
import constant.enums.MainNavigationEnum
import constant.enums.ViewEnum

@Composable
fun BaseViewTransition(
    secLastScreenKey: String,
    navigator: Navigator,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(
        durationMillis = 200,
        easing = LinearEasing,
    ),
    content: ScreenTransitionContent = { it.Content() }
) {
    BaseViewTransition(
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
private fun BaseViewTransition(
    secLastScreenKey: String,
    navigator: Navigator,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(
        durationMillis = 200,
        easing = LinearEasing,
    ),
    disposeScreenAfterTransitionEnd: Boolean = false,
    content: ScreenTransitionContent = { it.Content() }
) {
    ScreenTransition(
        navigator = navigator,
        transition = {

            if (navigator.lastItem.key.startsWith(ViewEnum.TAB_PARENT.code)
                && secLastScreenKey.startsWith(ViewEnum.PRE_LOAD.code)
            ) {
                fadeIn(
                    animationSpec = tween(300)
                ) togetherWith fadeOut(
                    animationSpec = tween(300)
                )
            } else {
                val (initialOffset, targetOffset) = when (navigator.lastEvent) {
                    StackEvent.Pop -> ({ size: Int -> -size }) to ({ size: Int -> size })
                    else -> ({ size: Int -> size }) to ({ size: Int -> -size })
                }
                slideInHorizontally(animationSpec, initialOffset) togetherWith
                        slideOutHorizontally(animationSpec, targetOffset)
            }
        },
        modifier = modifier,
        disposeScreenAfterTransitionEnd = disposeScreenAfterTransitionEnd,
        content = content,
    )
}

@Composable
fun TabTransition(
    secLastScreenKey: String,
    navigator: Navigator,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(
        durationMillis = 200,
        easing = LinearEasing,
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
    animationSpec: FiniteAnimationSpec<IntOffset> = tween(
        durationMillis = 200,
        easing = LinearEasing,
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
                fadeIn(
                    animationSpec = tween(300)
                ) togetherWith fadeOut(
                    animationSpec = tween(300)
                )
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

