import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.example.project.ui.HomeScreen
import org.example.project.ui.LoginScreen
import org.example.project.ui.SettingsScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

// Define tus pantallas como constantes o enums
enum class Screen {
    Home,
    Details,
    Settings
}
@Preview
@Composable
fun AppNavigation() {
    val currentScreen = remember { mutableStateOf<Screen>(Screen.Home) }

    when (currentScreen.value) {

        Screen.Home -> HomeScreen(onNavigateToDetails = {

            currentScreen.value = Screen.Details
        }, onNavigateToSettings = {
            currentScreen.value = Screen.Settings
        })

        Screen.Details -> LoginScreen(onNavigateBack = {
            currentScreen.value = Screen.Home
        })

        Screen.Settings -> SettingsScreen (onNavigateBack = {
            currentScreen.value = Screen.Home
        }

        )
    }
}

