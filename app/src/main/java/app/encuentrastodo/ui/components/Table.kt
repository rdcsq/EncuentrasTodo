package app.encuentrastodo.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LazyTable(
    modifier: Modifier = Modifier,
    columns: Int,
    rows: Int,
    headers: Array<String>,
    cellData: (Int, Int) -> String
) {
    LazyColumn(modifier) {
        item {
            Row {
                headers.forEach {
                    Text(it,
                        modifier = Modifier
                            .weight(1f / columns)
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }
            HorizontalDivider()
        }

        items(rows) { r ->
            Row {
                (0..<columns).forEach { c ->
                    Text(cellData(r, c),
                        modifier = Modifier
                            .weight(1f / columns)
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }
            HorizontalDivider()
        }
    }
}