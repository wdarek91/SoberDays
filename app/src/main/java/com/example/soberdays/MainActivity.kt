package com.example.soberdays

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soberdays.data.GeneralPrayer
import com.example.soberdays.data.PrayerRepository
import com.example.soberdays.data.SaintPatron
import com.example.soberdays.notification.DailyReminderReceiver
import com.example.soberdays.ui.MainViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            onResult = { }
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
            // Górny pasek
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
                        text = "Patroni i Modlitwy",
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

        // Dialog Modlitewnika i Patronów
        if (showPrayerDialog) {
            Dialog(
                onDismissRequest = { showPrayerDialog = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.94f)
                        .fillMaxHeight(0.92f),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF13161C))
                ) {
                    var selectedTab by remember { mutableStateOf(0) }
                    val tabs = listOf("Patroni trzeźwości", "Modlitwy ogólne")

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Belka górna
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "DUCHOWE WSPARCIE",
                                fontSize = 15.sp,
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

                        Spacer(modifier = Modifier.height(8.dp))

                        // Zakładki
                        TabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = Color.Transparent,
                            contentColor = MaterialTheme.colorScheme.primary,
                            indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        ) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index },
                                    text = {
                                        Text(
                                            text = title,
                                            fontSize = 13.sp,
                                            fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selectedTab == index) MaterialTheme.colorScheme.primary else Color.Gray
                                        )
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Zawartość zakładek
                        if (selectedTab == 0) {
                            // Lista patronów z wizerunkami
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                contentPadding = PaddingValues(vertical = 4.dp)
                            ) {
                                items(PrayerRepository.patrons) { patron ->
                                    SaintPatronCard(patron = patron)
                                }
                            }
                        } else {
                            // Lista modlitw ogólnych
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                contentPadding = PaddingValues(vertical = 4.dp)
                            ) {
                                items(PrayerRepository.generalPrayers) { prayer ->
                                    GeneralPrayerCard(prayer = prayer)
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

@Composable
fun SaintPatronCard(patron: SaintPatron) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1E26))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Wizerunek świętego w złotej obwódce
                Image(
                    painter = painterResource(id = patron.imageRes),
                    contentDescription = patron.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = patron.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = patron.subtitle,
                        fontSize = 12.sp,
                        color = Color(0xFFB5BAC4),
                        lineHeight = 15.sp
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Zwiń" else "Rozwiń",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    Divider(color = Color(0xFF2C313C), thickness = 0.8.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = patron.bio,
                        fontSize = 12.5.sp,
                        color = Color(0xFF9EACB9),
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = patron.prayerTitle,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = patron.prayerText,
                        fontSize = 13.5.sp,
                        fontFamily = FontFamily.Serif,
                        lineHeight = 21.sp,
                        color = Color(0xFFE8E8E8)
                    )
                }
            }
        }
    }
}

@Composable
fun GeneralPrayerCard(prayer: GeneralPrayer) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1E26))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = prayer.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = prayer.subtitle,
                fontSize = 12.sp,
                color = Color(0xFF9EACB9)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0xFF2C313C), thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = prayer.prayerText,
                fontSize = 13.5.sp,
                fontFamily = FontFamily.Serif,
                lineHeight = 21.sp,
                color = Color(0xFFE8E8E8)
            )
        }
    }
}
