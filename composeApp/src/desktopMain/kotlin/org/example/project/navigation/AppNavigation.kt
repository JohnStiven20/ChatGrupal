import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.example.project.navigation.Screen
import org.example.project.ui.chat.ChatGeneralScreen
import org.example.project.ui.chat.ChatPrivadoScreen
import org.example.project.ui.nickname.NickNameScreen
import org.example.project.ui.nickname.ViewModel

@Composable
fun AppNavigation(viewModel: ViewModel) {

    val pantallaActual = remember { mutableStateOf<Screen>(Screen.NickName) }

    when (pantallaActual.value) {

        Screen.NickName -> NickNameScreen(
            viewModel = viewModel,
            pantallaActual = pantallaActual
        )

        Screen.ChatGeneral -> ChatGeneralScreen(
            viewModel = viewModel,
            pantallaActual = pantallaActual
        )

        Screen.ChatPrivado -> {
            ChatPrivadoScreen(
                viewModel = viewModel,
                currentScreen = pantallaActual
            )
        }
    }
}