package com.withpeace.withpeace.core.designsystem.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.withpeace.withpeace.core.designsystem.theme.WithpeaceTheme

@Composable
fun WithpeaceCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        border = BorderStroke(width = 1.dp, color = WithpeaceTheme.colors.SystemGray2),
        colors = CardDefaults.cardColors(
            containerColor = WithpeaceTheme.colors.SystemWhite,
        ),
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun WithpeaceCardPreview() {
    WithpeaceTheme {
        WithpeaceCard {
            Text("haha")
        }
    }
}
