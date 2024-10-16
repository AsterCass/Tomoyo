package di


import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module


actual fun platformModule(): Module = module {

    single<Boolean>(qualifier = named("isMobile")) {
        true
    }

}