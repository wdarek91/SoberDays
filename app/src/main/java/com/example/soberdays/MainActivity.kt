package com.example.soberdays

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soberdays.notification.DailyReminderReceiver
import com.example.soberdays.ui.MainViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Harmonogram powiadomień o 8:00 rano
        DailyReminderReceiver.scheduleDaily8AmNotification(this)

        setContent {
            SoberTheme {
                RequestNotificationPermission()
                SoberMainScreen()
            }
        }
    }
}

@Composable
fun RequestNotificationPermission() {
    val context = LocalContext.current
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { /* Uprawnienie przyznane lub odrzucone */ }
        )

        LaunchedEffect(Unit) {
            val isGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!isGranted) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
fun SoberTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Color(0xFF0F1115),
            surface = Color(0xFF1A1D24),
            primary = Color(0xFFD4AF37),
            onBackground = Color(0xFFE6E6E6),
            onSurface = Color(0xFFB0B0B0)
        ),
        content = content
    )
}

@Composable
fun SoberMainScreen(viewModel: MainViewModel = viewModel()) {
    val state by viewModel.uiState.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }
    var showPrayerDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Górny pasek z przyciskiem modlitwy
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TRZEŹWOŚĆ",
                    fontSize = 14.sp,
                    letterSpacing = 4.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Przycisk MODLITWA
                OutlinedButton(
                    onClick = { showPrayerDialog = true },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Modlitwa",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Modlitwa",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Centralny licznik
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "${state.days}",
                    fontSize = 96.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "D N I",
                    fontSize = 18.sp,
                    letterSpacing = 6.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${state.hours} godz. ${state.minutes} min.",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Karta oszczędności & Cytat stoicki
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "ZAOSZCZĘDZONO (20 ZŁ / DZIEŃ)",
                            fontSize = 11.sp,
                            letterSpacing = 2.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format(Locale.getDefault(), "%.2f zł", state.savedMoney),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Text(
                    text = "„${state.quote}”",
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Przycisk Resetu
            OutlinedButton(
                onClick = { showResetDialog = true },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF7A7E85)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Zresetuj licznik", fontSize = 13.sp)
            }
        }

        // Dialog Modlitwy
        if (showPrayerDialog) {
            Dialog(onDismissRequest = { showPrayerDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF161920))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "MODLITWY",
                                fontSize = 16.sp,
                                letterSpacing = 3.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            IconButton(onClick = { showPrayerDialog = false }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Zamknij",
                                    tint = Color.LightGray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            // Modlitwa 1: O pogodę ducha
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E222B)),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Modlitwa o pogodę ducha",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Boże, użycz mi pogody ducha,\nabym godził się z tym, czego nie mogę zmienić,\nodwagi, abym zmieniał to, co mogę zmienić,\ni mądrości, abym odróżniał jedno od drugiego.",
                                        fontSize = 14.sp,
                                        lineHeight = 22.sp,
                                        fontFamily = FontFamily.Serif,
                                        color = Color(0xFFE2E2E2)
                                    )
                                }
                            }

                            // Modlitwa 2: Do św. Jana Chrzciciela
                            Card(
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E222B)),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "Modlitwa do św. Jana Chrzciciela",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Święty Janie Chrzcicielu, który abstynencję praktykowałeś przez całe swoje życie,\npomóż, abyśmy w naszej Ojczyźnie dożyli czasów,\nw których alkohol przestanie być symbolem nieszczęść, zmartwień i tragedii.\n\nChroń nas przed grzechem upijania się. Bądź natchnieniem i pomocą dla wszystkich,\nktórzy wyrzekają się napojów alkoholowych dla dobra swoich najbliższych i Narodu.\n\nPatronie trzeźwości, wzbudź w sercach moich rodaków pragnienie życia bez alkoholu,\nabyśmy poprzez ich abstynencję osiągnęli trzeźwość całego Narodu.\n\nAmen.",
                                        fontSize = 14.sp,
                                        lineHeight = 22.sp,
                                        fontFamily = FontFamily.Serif,
                                        color = Color(0xFFE2E2E2)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dialog potwierdzenia resetu
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = { Text("Zresetować postęp?") },
                text = { Text("Pamiętaj: potknięcie nie przekreśla drogi, którą już przeszedłeś. Zacznij ponownie teraz.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.reset()
                            showResetDialog = false
                        }
                    ) {
                        Text("Resetuj", color = Color(0xFFE57373))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showResetDialog = false }) {
                        Text("Anuluj")
                    }
                }
            )
        }
    }
}
