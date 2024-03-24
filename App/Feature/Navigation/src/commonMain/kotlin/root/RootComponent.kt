package ru.online.education.app.feature.navigation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.webhistory.WebHistoryController
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.sample.shared.multipane.MultiPaneComponent
import deepLinking.DeepLink
import destination.course.DefaultMultiPaneComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import model.AppState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repository.AccountRepository
import root.AuthDialogComponent
import root.ComposeComponent
import ru.online.education.app.core.util.coruotines.DispatcherProvider
import ru.online.education.app.feature.account.domain.repository.UserAuthStore
import ru.online.education.app.feature.account.presentation.model.AuthState
import util.ApiResult

@OptIn(ExperimentalDecomposeApi::class)
class RootComponent(
    context: ComponentContext,
    deepLink: DeepLink = DeepLink.None,
    webHistoryController: WebHistoryController? = null,
    private val scope: CoroutineScope = CoroutineScope(DispatcherProvider.Main + SupervisorJob())
) : ComposeComponent,
    KoinComponent,
    ComponentContext by context {
    private val navigation = StackNavigation<Config>()
    private val dialogNavigation = SlotNavigation<Unit>()

    val accountRepository by inject<AccountRepository>()

    val authStorage by inject<UserAuthStore>()
    val appState = MutableValue(AppState())

    init {
        scope.launch(DispatcherProvider.IO) {
            updateAuthState()
        }
    }

    private suspend fun updateAuthState() {
        authStorage.readAsFlow().collect { authData ->
            val currentUser = accountRepository.currentUser()
            val newAuthState = if (currentUser is ApiResult.Success) AuthState.LoggedIn(
                login = currentUser.data.email,
                displayName = currentUser.data.firstName
            ) else AuthState.LoggedOut

            appState.update { it.copy(authState = newAuthState) }
        }
    }

    val dialog = childSlot(
        source = dialogNavigation,
        serializer = null,
        handleBackButton = true
    ) { configuration, componentContext ->
        AuthDialogComponent(
            context = componentContext,
            onDissmissed = {
                dialogNavigation.dismiss()
            },
        )
    }

    fun openAuthDialog() {
        dialogNavigation.activate(Unit)
    }

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
            Config.Courses -> Child.Courses(
                component = DefaultMultiPaneComponent(componentContext)
            )
        }

    fun navigateUp(config: Config) {
        navigation.bringToFront(config)
    }

    fun accountClick() {
        if (appState.value.authState is AuthState.LoggedIn) {
            navigation.bringToFront(Config.Account)
        } else {
            openAuthDialog()
        }
    }

    fun logOutClick() {
        scope.launch(DispatcherProvider.IO) {
            accountRepository.logOut()
        }
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
            Config.Courses -> Child.Courses.name
        }

    private fun getConfigForPath(path: String): Config =
        when (path.removePrefix("/").lowercase()) {
            Child.AccountChild.name.lowercase() -> Config.Account
            Child.HomeChild.name.lowercase() -> Config.Home
            Child.Courses.name.lowercase() -> Config.Courses
            "" -> Config.Home
            else -> Config.Home
        }


    sealed class Child(
        name: String? = null
    ) {

        data object HomeChild : Child() {
            override fun toConfig(): Config = Config.Home
            val name = "Home"
        }

        data object AccountChild : Child() {
            override fun toConfig(): Config = Config.Account
            val name: String = "Account"

        }

        data object NotFound : Child() {
            override fun toConfig(): Config = Config.NotFound
            val name: String = "NotFound"
        }

        data class Courses(
            val component: MultiPaneComponent
        ) : Child() {
            override fun toConfig(): Config = Config.Courses

            companion object {
                val name: String = "Courses"
            }
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

        @Serializable
        data object Courses : Config()

        companion object {
            val all: List<Config>
                get() = listOf(
                    Home,
                    Account,
                    Courses
                )
        }
    }


}
