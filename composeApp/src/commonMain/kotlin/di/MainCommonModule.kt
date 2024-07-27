package di


import data.model.ArticleScreenModel
import data.model.MainScreenModel
import data.model.MusicScreenModel
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


}