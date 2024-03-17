package ru.online.education.app.feature.navigation.root

import androidx.compose.ui.text.toLowerCase
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.webhistory.WebHistoryController
import com.arkivanov.decompose.value.Value
import deepLinking.DeepLink
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import root.ComposeComponent

@OptIn(ExperimentalDecomposeApi::class)
class RootComponent constructor(
    context: ComponentContext,
    deepLink: DeepLink = DeepLink.None,
    webHistoryController: WebHistoryController? = null,
) : ComposeComponent, ComponentContext by context {


    private val navigation = StackNavigation<Config>()


    val childStack by lazy {
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialStack = {
                getInitialStack(webHistoryPaths = webHistoryController?.historyPaths, deepLink = deepLink)
            },
            childFactory = ::child
        )
    }

    init {
        webHistoryController?.attach(
            navigator = navigation,
            stack = childStack,
            serializer = Config.serializer(),
            getPath = ::getPathForConfig,
            getConfiguration = ::getConfigForPath,
        )
    }

    private fun child(config: Config, componentContext: ComponentContext): Child =
        when (config) {
            Config.Account -> Child.AccountChild
            Config.Home -> Child.HomeChild
            Config.NotFound -> Child.NotFound
        }

    fun navigateUp(config: Config) {
        navigation.bringToFront(config)
    }


    private fun getInitialStack(webHistoryPaths: List<String>?, deepLink: DeepLink): List<Config> =
        webHistoryPaths
            ?.takeUnless(List<*>::isEmpty)
            ?.map(::getConfigForPath)
            ?: getInitialStack(deepLink)

    private fun getInitialStack(deepLink: DeepLink): List<Config> =
        when (deepLink) {
            is DeepLink.None -> listOf(Config.Home)
            is DeepLink.Web -> listOf(getConfigForPath(deepLink.path))
        }

    private fun getPathForConfig(config: Config): String =
        when (config) {
            Config.Account -> Child.AccountChild.name
            Config.Home -> Child.HomeChild.name
            Config.NotFound -> Child.NotFound.name
        }

    private fun getConfigForPath(path: String): Config =
        when (path.removePrefix("/").lowercase()) {
            "AccountChild".lowercase() -> Config.Account
            "HomeChild".lowercase() -> Config.Home
            "" -> Config.Home
            else -> Config.Home
        }


    sealed class Child(
        name: String? = null
    ) {
        val name: String = name ?: this::class.simpleName ?: throw NullPointerException()

        data object HomeChild : Child() {
            override fun toConfig(): Config = Config.Home
        }

        data object AccountChild : Child() {
            override fun toConfig(): Config = Config.Account
        }

        data object NotFound : Child() {
            override fun toConfig(): Config = Config.NotFound
        }

        abstract fun toConfig(): Config
    }

    @Serializable
    sealed class Config {

        @Serializable
        data object Home : Config()

        @Serializable
        data object Account : Config()

        @Serializable
        data object NotFound : Config()

        companion object {
            val all: List<Config>
                get() = listOf(
                    Home,
                    Account
                )
        }
    }


}
