package di


import com.github.panpf.sketch.decode.GifSkiaAnimatedDecoder
import com.github.panpf.sketch.request.ImageRequest
import data.store.SettingsWrapper
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single { SettingsWrapper().createSettings() }

    single<(ImageRequest.Builder.() -> Unit)> {
        {
            components {
                addDecoder(GifSkiaAnimatedDecoder.Factory())
            }
        }
    }
}