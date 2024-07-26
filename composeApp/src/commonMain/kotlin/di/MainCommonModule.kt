package di


import data.model.ArticleScreenModel
import org.koin.dsl.module

fun commonModule() = module {

    single<ArticleScreenModel> {
        ArticleScreenModel()
    }


}