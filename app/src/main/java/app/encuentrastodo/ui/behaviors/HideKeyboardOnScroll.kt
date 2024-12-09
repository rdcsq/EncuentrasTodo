package app.encuentrastodo.ui.behaviors

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.SoftwareKeyboardController

class HideKeyboardOnScroll(private val keyboardController: SoftwareKeyboardController?) :
    NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        keyboardController?.hide()
        return super.onPreScroll(available, source)
    }
}