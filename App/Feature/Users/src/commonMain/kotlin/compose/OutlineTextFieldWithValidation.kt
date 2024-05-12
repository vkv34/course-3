package compose
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


/**
 *
 * Компонент, отображающий текстовое поле с валидацией.
 * @param value Текущее значение текстового поля.
 * @param onValueChange Функция обратного вызова, вызываемая при изменении значения текстового поля.
 * @param modifier Модификатор для данного компонента.
 * @param enabled Определяет, доступно ли текстовое поле для взаимодействия.
 * @param readOnly Определяет, является ли текстовое поле только для чтения.
 * @param textStyle Стиль текста для текстового поля.
 * @param label Метка, отображаемая над текстовым полем.
 * @param placeholder Заглушка, отображаемая внутри текстового поля в отсутствие значения.
 * @param leadingIcon Иконка, отображаемая в начале текстового поля.
 * @param trailingIcon Иконка, отображаемая в конце текстового поля.
 * @param isError Определяет, должно ли текстовое поле указывать на ошибку.
 * @param visualTransformation Визуальное преобразование для отображения значения текстового поля.
 * @param keyboardOptions Параметры клавиатуры для текстового поля.
 * @param keyboardActions Действия клавиатуры для текстового поля.
 * @param singleLine Определяет, должно ли текстовое поле быть однострочным.
 * @param maxLines Максимальное количество строк для текстового поля.
 * @param interactionSource Интерактивный источник для текстового поля.
 * @param shape Форма обводки текстового поля.
 * @param colors Цвета для текстового поля.
 * @param errorMsg Сообщение об ошибке, отображаемое под текстовым полем в случае ошибки.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlineTextFieldWithValidation(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    errorMsg : String = ""
){

    Column(
        modifier = Modifier
            .padding(
                bottom = if (isError) {
                    0.dp
                } else {
                    10.dp
                }
            )
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            enabled = enabled,
            readOnly = readOnly,
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            singleLine = singleLine,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors
        )

        if (isError){
            Text(
                text = errorMsg,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }

}