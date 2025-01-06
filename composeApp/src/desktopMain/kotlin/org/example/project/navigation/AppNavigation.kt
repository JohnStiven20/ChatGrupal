import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.example.project.navigation.Screen
import org.example.project.ui.chat.ChatGeneral
import org.example.project.ui.chat.ChatPersonal1
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

        Screen.ChatGeneral -> ChatGeneral(
            viewModel = viewModel,
            currentScreen = pantallaActual
        )

        Screen.ChatPrivado -> {
            ChatPersonal1(
                viewModel = viewModel,
                currentScreen = pantallaActual
            )
        }
    }
}