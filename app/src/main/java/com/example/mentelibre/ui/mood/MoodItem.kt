package com.example.mentelibre.ui.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mentelibre.data.mood.MoodType

@Composable
fun MoodItem(
    mood: MoodType,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .size(90.dp)
            .background(
                if (selected) mood.color.copy(alpha = 0.3f) else Color.White,
                CircleShape
            )
            .border(
                width = if (selected) 3.dp else 1.dp,
                color = if (selected) mood.color else Color.LightGray,
                shape = CircleShape
            )
            .clickable { onClick() }
            .padding(12.dp)
    ) {

        Icon(
            painter = painterResource(id = mood.icon),
            contentDescription = mood.label,
            tint = Color.Unspecified,
            modifier = Modifier.size(40.dp)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = mood.label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
