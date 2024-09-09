package di


import com.github.panpf.sketch.decode.GifSkiaAnimatedDecoder
import com.github.panpf.sketch.request.ImageRequest
import com.russhwolf.settings.ExperimentalSettingsApi
import data.store.SettingsWrapper
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
actual fun platformModule(): Module = module {
    single { SettingsWrapper().createSettings() }

    single<(ImageRequest.Builder.() -> Unit)> {
        {
            components {
                addDecoder(GifSkiaAnimatedDecoder.Factory())
            }
        }
    }

    single<Boolean>(qualifier = named("isMobile")) {
        false
    }
}