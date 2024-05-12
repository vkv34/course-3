package navgation.destination.root.account

import androidx.compose.runtime.Composable
import compose.UpdateUserScreen
import destination.AccountScreenComponent

@Composable
fun AccountContent(
    accountScreenComponent: AccountScreenComponent
) {
    UpdateUserScreen(
        viewModel = accountScreenComponent.viewModel,
        onSuccess = {}
    )
}