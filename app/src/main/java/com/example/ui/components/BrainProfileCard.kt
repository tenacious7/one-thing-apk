package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.model.BrainProfile
import com.example.ui.theme.InkBlack
import com.example.ui.theme.PaperWhite

@Composable
fun BrainProfileCard(profile: BrainProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, InkBlack),
        colors = CardDefaults.cardColors(containerColor = PaperWhite)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Level ${profile.level}", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            Text(text = profile.archetype, style = androidx.compose.material3.MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Next Unlock: ${profile.nextUnlock}", style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
        }
    }
}
