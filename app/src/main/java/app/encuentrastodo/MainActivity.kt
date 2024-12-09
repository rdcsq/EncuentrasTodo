package app.encuentrastodo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import app.encuentrastodo.ui.EncuentrasTodoTheme
import app.encuentrastodo.ui.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EncuentrasTodoTheme {
                Surface(
                    Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .safeDrawingPadding()
                ) {
                    Navigation()
                }
            }
        }
    }
}
