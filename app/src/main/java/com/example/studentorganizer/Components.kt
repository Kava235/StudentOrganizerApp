@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.studentorganizer

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun AppBar(
    title: String,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { Text(title, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        }
    )
}

private data class BottomItem(val screen: Screen, val label: String, val icon: ImageVector)

private val bottomItems = listOf(
    BottomItem(Screen.Dashboard, "Dashboard", Icons.Default.Home),
    BottomItem(Screen.Assignments, "Assignments", Icons.AutoMirrored.Filled.List),
    BottomItem(Screen.Profile, "Profile", Icons.Default.Person),
    BottomItem(Screen.Settings, "Settings", Icons.Default.Settings)
)

@Composable
fun BottomBar(
    navController: NavHostController,
    currentRoute: String?
) {
    NavigationBar {
        bottomItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.screen.route,
                onClick = {
                    if (currentRoute != item.screen.route) {
                        navController.navigate(item.screen.route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}

@Composable
fun CourseInfo(
    courseName: String,
    courseCode: String,
    instructor: String,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.size(width = 240.dp, height = 150.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(courseCode, style = MaterialTheme.typography.labelLarge)
            Text(courseName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(instructor, style = MaterialTheme.typography.bodySmall)
            Text("Progress: ${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelMedium)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PriorityBadge(priority: String) {
    val color = when (priority) {
        "High" -> Color(0xFFD32F2F)
        "Medium" -> Color(0xFFF57C00)
        else -> Color(0xFF388E3C)
    }
    Surface(shape = RoundedCornerShape(50), color = color) {
        Text(
            text = priority,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun AssignmentCard(
    title: String,
    course: String,
    dueDate: String,
    priority: String,
    isCompleted: Boolean,
    onToggleComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = isCompleted, onCheckedChange = { onToggleComplete() })
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
                Text("$course  •  Due $dueDate", style = MaterialTheme.typography.bodySmall)
            }
            PriorityBadge(priority)
        }
    }
}

@Composable
fun StudentProfile(
    studentName: String,
    studentId: String,
    major: String,
    avatarRes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = avatarRes),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(studentName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("ID: $studentId", style = MaterialTheme.typography.bodyMedium)
                Text(major, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun AssignmentForm(
    title: String,
    course: String,
    dueDate: String,
    priority: String,
    description: String,
    reminder: Boolean,
    onTitleChange: (String) -> Unit,
    onCourseChange: (String) -> Unit,
    onDueDateChange: (String) -> Unit,
    onPriorityChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onReminderChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var courseMenuOpen by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 1. Title (required, single line)
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Assignment Title *") },
            singleLine = true,
            isError = title.isBlank(),
            supportingText = { if (title.isBlank()) Text("Required") },
            modifier = Modifier.fillMaxWidth()
        )

        // 2. Course dropdown
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = course,
                onValueChange = {},
                readOnly = true,
                label = { Text("Course Name / Code *") },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = "Select course") },
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable { courseMenuOpen = true }
            )
            DropdownMenu(expanded = courseMenuOpen, onDismissRequest = { courseMenuOpen = false }) {
                courseOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onCourseChange(option)
                            courseMenuOpen = false
                        }
                    )
                }
            }
        }

        // 3. Due date with trailing calendar icon
        OutlinedTextField(
            value = dueDate,
            onValueChange = onDueDateChange,
            label = { Text("Due Date *") },
            placeholder = { Text("e.g. 25 Nov 2026") },
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Pick date")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // 4. Priority radio buttons
        Text("Priority Level", style = MaterialTheme.typography.titleSmall)
        Row(verticalAlignment = Alignment.CenterVertically) {
            priorityOptions.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onPriorityChange(option) }
                ) {
                    RadioButton(selected = priority == option, onClick = { onPriorityChange(option) })
                    Text(option, modifier = Modifier.padding(end = 12.dp))
                }
            }
        }

        // 5. Description (multi-line)
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description / Notes") },
            minLines = 3,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth()
        )

        // 6. Reminder checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onReminderChange(!reminder) }
        ) {
            Checkbox(checked = reminder, onCheckedChange = onReminderChange)
            Text("Enable Reminder Notification")
        }
    }

    if (showDatePicker) {
        val pickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { onDueDateChange(formatDate(it)) }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }
}

private fun formatDate(millis: Long): String {
    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(Date(millis))
}
