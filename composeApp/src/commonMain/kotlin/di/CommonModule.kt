package di


import com.russhwolf.settings.ExperimentalSettingsApi
import data.model.ArticleScreenModel
import data.model.ChatScreenModel
import data.model.ContactScreenModel
import data.model.GlobalDataModel
import data.model.MainScreenModel
import data.model.MusicScreenModel
import data.store.DataStorageManager
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
fun commonModule() = module {

    //global
    single<GlobalDataModel> {
        GlobalDataModel()
    }

    //common
    single<ArticleScreenModel> {
        ArticleScreenModel()
    }

    single<ChatScreenModel> {
        ChatScreenModel(dataStorageManager = get())
    }

    single<ContactScreenModel> {
        ContactScreenModel()
    }

    single<MusicScreenModel> {
        MusicScreenModel(dataStorageManager = get())
    }

    //main
    single<MainScreenModel> {
        MainScreenModel()
    }

    //db
    single<DataStorageManager> {
        DataStorageManager(settings = get())
    }

}

expect fun platformModule(): Module