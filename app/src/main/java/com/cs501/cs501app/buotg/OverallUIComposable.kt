package com.cs501.cs501app.buotg

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shapeSize : Dp = 10.dp,
) {
    val colors = ButtonDefaults.buttonColors(
        backgroundColor = Color(0xFFF52D4D) ,
        contentColor = Color.White
    )
    Button(
        onClick = onClick,
        colors = colors,
        modifier = modifier,
        shape = RoundedCornerShape(shapeSize),
        enabled = enabled
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button
        )
    }
}

@Composable
fun CustomButtonWithIcon(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shapeSize: Dp,
    icon: ImageBitmap
) {
    val colors = ButtonDefaults.buttonColors(
        backgroundColor = Color(0xFFF52D4D),
        contentColor = Color.White
    )
    Button(
        onClick = onClick,
        colors = colors,
        modifier = modifier,
        shape = RoundedCornerShape(shapeSize),
        enabled = enabled
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                bitmap = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.button,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}




@Composable
fun CustomText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null,
    color: Color = Color(0xFFF52D4D)

) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight
    )
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = modifier,
        textStyle = MaterialTheme.typography.body1,
        shape = RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.textFieldColors(
            unfocusedLabelColor = Color.Black,
//            focusedLabelColor = Color.White,
            backgroundColor = Color(0xFFFFCDD2),
            cursorColor = Color.Black,
            focusedIndicatorColor = Color(0xFFF52D4D) ,
            unfocusedIndicatorColor = Color(0xFFFFCDD2)
        ),
        singleLine = true,
        placeholder = placeholder,
        visualTransformation = visualTransformation
    )
}

