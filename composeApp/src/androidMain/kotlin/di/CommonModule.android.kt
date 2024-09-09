package di


import android.os.Build
import com.aster.yuno.tomoyo.MainActivity
import com.github.panpf.sketch.decode.GifAnimatedDecoder
import com.github.panpf.sketch.decode.GifMovieDecoder
import com.github.panpf.sketch.request.ImageRequest
import com.russhwolf.settings.ExperimentalSettingsApi
import data.store.SettingsWrapper
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
actual fun platformModule(): Module = module {
    single {
        SettingsWrapper(
            context = MainActivity.mainContext!!
        ).createSettings()
    }

    single<(ImageRequest.Builder.() -> Unit)> {
        {
            components {
                addDecoder(
                    when {
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> GifAnimatedDecoder.Factory()
                        else -> GifMovieDecoder.Factory()
                    }
                )
            }
        }
    }

    single<Boolean>(qualifier = named("isMobile")) {
        true
    }

}