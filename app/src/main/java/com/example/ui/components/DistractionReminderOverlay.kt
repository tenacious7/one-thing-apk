package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AcidLime
import com.example.ui.theme.CharcoalMuted
import com.example.ui.theme.DarkCharcoal
import com.example.ui.theme.InkBlack
import com.example.ui.theme.PaperWhite
import com.example.ui.theme.TangerineOrange
import com.example.ui.theme.WarmCreamSurface

/**
 * Friendly overlay reminder shield warning the user when a distracting app is accessed during an active focus session.
 */
@Composable
fun DistractionReminderOverlay(
    blockedAppName: String,
    remainingSeconds: Int,
    onReturnToApp: () -> Unit,
    onEmergencyExit: () -> Unit
) {
    Dialog(onDismissRequest = { /* Modal: require explicit choice */ }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .border(2.dp, InkBlack, RoundedCornerShape(20.dp))
                .testTag("distraction_reminder_overlay"),
            color = WarmCreamSurface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(InkBlack),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = TangerineOrange,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "FOCUS SHIELD ACTIVE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.dp.value.sp,
                        color = TangerineOrange
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "$blockedAppName is Restricted",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = InkBlack,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                val m = remainingSeconds / 60
                val s = remainingSeconds % 60
                Text(
                    text = String.format("%02d:%02d REMAINING IN FOCUS SESSION", m, s),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = InkBlack
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "You made a promise to your future self. Complete today's ONE THING to unlock a 5-minute reward pass.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = CharcoalMuted,
                        textAlign = TextAlign.Center
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onReturnToApp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("shield_return_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = InkBlack,
                        contentColor = PaperWhite
                    )
                ) {
                    Text("Return to ONE THING", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onEmergencyExit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("shield_emergency_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkCharcoal)
                ) {
                    Icon(Icons.Default.Emergency, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Emergency Exit (Pause Focus)", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
