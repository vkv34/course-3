package ru.online.education.app.feature.home.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Navigation Item class
 *
 * @param name Item Name
 * @param icon Item display icon
 * @param navigationGroup Using for Groupping navigation destination
 * @param onClick navigation item click handler
 */
data class NavigationItem(
    val name: String,
    val icon: ImageVector,
    val navigationGroup: NavigationGroup = NavigationGroup.mainGroup,
    val onClick: () -> Unit,
) {
    override fun equals(other: Any?): Boolean = if (other is NavigationItem) {
        this.name == other.name
    } else false
    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + icon.hashCode()
        result = 31 * result + navigationGroup.hashCode()
        return result
    }
}


data class NavigationGroup(
    val groupeName: String
) {
    companion object {
        val mainGroup = NavigationGroup("Основное")
        val accountGroup = NavigationGroup("Аккаунт")
        val menuGroup = NavigationGroup("Меню")
        val invisibleGroup = NavigationGroup("")
    }
}
