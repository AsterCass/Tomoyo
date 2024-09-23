package di


import com.github.panpf.sketch.decode.GifSkiaAnimatedDecoder
import com.github.panpf.sketch.request.ImageRequest
import com.russhwolf.settings.ExperimentalSettingsApi
import data.store.SettingsWrapper
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

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

    single<BufferedImage>(qualifier = named("showIcon")) {
        ImageIO.read(
            Thread.currentThread().contextClassLoader
                .getResource("logo_pro_round.png")
        )

    }

    single<BufferedImage>(qualifier = named("hideIcon")) {
        ImageIO.read(
            Thread.currentThread().contextClassLoader
                .getResource("notification-lightning.png")
        )


    }
}