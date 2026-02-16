package ci.nsu.moble.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ci.nsu.moble.main.ui.theme.PracticeTheme

// Карта цветов (исправляем дубликат Orange)
private val colorMap = mapOf(
    "Red" to Color.Red,
    "Orange" to Color(0xFFFFA500), // Оранжевый
    "Yellow" to Color.Yellow,
    "Green" to Color.Green,
    "Blue" to Color.Blue,
    "Indigo" to Color(0xFF4B0082), // Индиго
    "Violet" to Color(0xFFEE82EE), // Фиолетовый
    "Cyan" to Color.Cyan,
    "Magenta" to Color.Magenta,
    "Black" to Color.Black,
    "White" to Color.White,
    "Gray" to Color.Gray,
    "DarkGray" to Color.DarkGray,
    "LightGray" to Color.LightGray
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ColorSearchScreen()
                }
            }
        }
    }
}

@Composable
fun ColorSearchScreen() {
    var searchQuery by remember { mutableStateOf("") }
    var buttonColor by remember { mutableStateOf(Color(0xFF6200EE)) } // Начальный цвет кнопки
    val context = LocalContext.current
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Поле ввода
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                showError = false
            },
            label = { Text("Введите название цвета") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = showError
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка поиска
        Button(
            onClick = {
                val foundColor = colorMap[searchQuery]
                if (foundColor != null) {
                    buttonColor = foundColor
                    Log.d("ColorSearch", "Цвет '$searchQuery' найден и применен")
                    showError = false
                } else {
                    Log.d("ColorSearch", "Пользовательский цвет '$searchQuery' не найден")
                    showError = true
                    errorMessage = "Цвет '$searchQuery' не найден"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(buttonColor, RoundedCornerShape(8.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor
            )
        ) {
            Text("Найти цвет", color = Color.White)
        }

        // Сообщение об ошибке
        if (showError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Заголовок палитры
        Text(
            text = "Палитра цветов:",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Список палитры
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(colorMap.toList()) { (colorName, color) ->
                ColorItem(
                    colorName = colorName,
                    color = color,
                    onColorSelected = {
                        searchQuery = colorName
                    }
                )
            }
        }
    }
}

@Composable
fun ColorItem(
    colorName: String,
    color: Color,
    onColorSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        onClick = onColorSelected
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Цветной квадрат
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color, RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Название цвета
            Text(
                text = colorName,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            // Код цвета в HEX
            Text(
                text = color.toHexString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

// Вспомогательная функция для преобразования Color в HEX строку
fun Color.toHexString(): String {
    val red = (this.red * 255).toInt()
    val green = (this.green * 255).toInt()
    val blue = (this.blue * 255).toInt()
    val alpha = (this.alpha * 255).toInt()

    return if (alpha < 255) {
        String.format("#%02X%02X%02X%02X", alpha, red, green, blue)
    } else {
        String.format("#%02X%02X%02X", red, green, blue)
    }
}

@Preview(showBackground = true)
@Composable
fun ColorSearchScreenPreview() {
    PracticeTheme {
        ColorSearchScreen()
    }
}