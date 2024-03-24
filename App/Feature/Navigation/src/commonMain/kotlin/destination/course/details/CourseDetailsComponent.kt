package com.arkivanov.sample.shared.multipane.details

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow

class CourseDetailsComponent(
    componentContext: ComponentContext,
    articleId: Long,
    isToolbarVisible: Flow<Boolean>,
    private val onFinished: () -> Unit
) : ComponentContext by componentContext{

//    private val _models =
//        MutableValue(
//            Model(
//                isToolbarVisible = false,
//                article = database.getById(id = articleId).toArticle()
//            )
//        )
//
//    override val models: Value<Model> = _models
//
//    init {
//        isToolbarVisible.subscribeScoped { isVisible ->
//            _models.update { it.copy(isToolbarVisible = isVisible) }
//        }
//    }
//
//    private fun ArticleEntity.toArticle(): Article =
//        Article(
//            title = title,
//            text = text
//        )
//
//    override fun onCloseClicked() {
//        onFinished()
//    }
}
