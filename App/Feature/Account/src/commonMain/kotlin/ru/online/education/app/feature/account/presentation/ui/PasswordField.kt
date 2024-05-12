package ru.online.education.app.feature.account.presentation.ui
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

/**
 *
 * Компонент, отображающий поле для ввода пароля.
 *
 * @param modifier Модификатор для данного компонента.
 *
 * @param value Текущее значение поля для ввода пароля.
 *
 * @param isError Определяет, должно ли поле для ввода пароля указывать на ошибку.
 *
 * @param labelText Текст метки, отображаемой над полем для ввода пароля.
 *
 * @param onValueChange Функция обратного вызова, вызываемая при изменении значения поля для ввода пароля.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    modifier: Modifier = Modifier,
    value: String = "",
    isError: Boolean = false,
    labelText: String = "Пароль",
    onValueChange: (String) -> Unit,
) {
    var isVisible by remember { mutableStateOf(false) }


    OutlinedTextField(
        value = value,
        shape = MaterialTheme.shapes.small,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        isError = isError,
        label = {
            Text(text = labelText)
        },
        modifier = modifier,
        trailingIcon = {
            Icon(imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                contentDescription = "visibility icon",
                Modifier.clickable {
                    isVisible = ! isVisible
                })
        },
        visualTransformation = if (isVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        onValueChange = {
            onValueChange(it)
        })
}