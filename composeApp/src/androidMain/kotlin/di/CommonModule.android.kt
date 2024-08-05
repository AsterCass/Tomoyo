package di


import com.aster.yuno.tomoyo.MainActivity
import data.store.SettingsWrapper
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single {
        SettingsWrapper(
            context = MainActivity.mainContext!!
        ).createSettings()
    }

}