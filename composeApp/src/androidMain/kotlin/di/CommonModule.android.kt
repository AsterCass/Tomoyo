package di


import com.aster.yuno.tomoyo.MainActivity
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

    single<Boolean>(qualifier = named("isMobile")) {
        true
    }

}