package di


import data.model.ArticleScreenModel
import data.model.MainScreenModel
import data.model.MusicScreenModel
import data.store.DataStorageManager
import org.koin.core.module.Module
import org.koin.dsl.module

fun commonModule() = module {

    single<MainScreenModel> {
        MainScreenModel()
    }

    single<ArticleScreenModel> {
        ArticleScreenModel()
    }

    single<MusicScreenModel> {
        MusicScreenModel()
    }

    single<DataStorageManager> {
        DataStorageManager(settings = get())
    }

}

expect fun platformModule(): Module