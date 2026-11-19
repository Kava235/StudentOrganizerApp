package com.example.studentorganizer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.widget.Toast

@Composable
fun DashboardScreen(
    studentName: String,
    courses: List<Course>,
    assignments: List<Assignment>,
    onToggleComplete: (Int) -> Unit,
    onAddClick: () -> Unit
) {
    val pending = assignments.filter { !it.isCompleted }
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            AppBar(
                title = "Welcome back, ${studentName.substringBefore(' ')}!",
                canNavigateBack = false,
                onBackClick = {}
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add assignment")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Quick stats row
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Active Courses (${courses.size})", Modifier.weight(1f))
                StatCard("Pending Tasks (${pending.size})", Modifier.weight(1f))
            }

            // Active courses (horizontal LazyRow)
            Text("Active Courses", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(courses) { course ->
                    CourseInfo(
                        courseName = course.name,
                        courseCode = course.code,
                        instructor = course.instructor,
                        progress = course.progress
                    )
                }
            }

            // Urgent deadlines (top 2 pending)
            Text("Urgent Deadlines", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                pending.take(2).forEach { a ->
                    AssignmentCard(
                        title = a.title,
                        course = a.course,
                        dueDate = a.dueDate,
                        priority = a.priority,
                        isCompleted = a.isCompleted,
                        onToggleComplete = { onToggleComplete(a.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(text: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.elevatedCardColors()
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
fun AssignmentsScreen(
    assignments: List<Assignment>,
    onToggleComplete: (Int) -> Unit,
    onAddClick: () -> Unit
) {
    var filter by remember { mutableStateOf(AssignmentFilter.All) }
    val visible = when (filter) {
        AssignmentFilter.All -> assignments
        AssignmentFilter.Pending -> assignments.filter { !it.isCompleted }
        AssignmentFilter.Completed -> assignments.filter { it.isCompleted }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = { AppBar(title = "My Assignments", canNavigateBack = false, onBackClick = {}) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add assignment")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Filter row (RadioButtons)
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssignmentFilter.entries.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.selectable(
                            selected = filter == option,
                            role = Role.RadioButton,
                            onClick = { filter = option }
                        )
                    ) {
                        RadioButton(selected = filter == option, onClick = null)
                        Text(option.label, modifier = Modifier.padding(start = 4.dp, end = 16.dp, top = 12.dp, bottom = 12.dp))
                    }
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(visible, key = { it.id }) { a ->
                    AssignmentCard(
                        title = a.title,
                        course = a.course,
                        dueDate = a.dueDate,
                        priority = a.priority,
                        isCompleted = a.isCompleted,
                        onToggleComplete = { onToggleComplete(a.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = { AppBar(title = "Student Profile", canNavigateBack = false, onBackClick = {}) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StudentProfile(
                studentName = sampleStudentName,
                studentId = sampleStudentId,
                major = sampleMajor,
                avatarRes = R.drawable.ic_avatar
            )

            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Academic Stats", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("GPA: 3.82")
                    Text("Completed Credits: 84")
                    Text("Current Semester: Fall 2026")
                }
            }

            Button(
                onClick = { Toast.makeText(context, "Edit Profile clicked", Toast.LENGTH_SHORT).show() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Edit Profile") }

            OutlinedButton(
                onClick = { Toast.makeText(context, "Exporting academic summary...", Toast.LENGTH_SHORT).show() },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Export Academic Summary") }
        }
    }
}

@Composable
fun SettingsScreen(settings: AppSettings) {
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = { AppBar(title = "Settings", canNavigateBack = false, onBackClick = {}) }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dark mode (Switch)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Dark Mode", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = settings.darkMode, onCheckedChange = { settings.darkMode = it })
            }

            // Push notifications (Checkbox)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Push Notifications", style = MaterialTheme.typography.bodyLarge)
                Checkbox(
                    checked = settings.pushNotifications,
                    onCheckedChange = { settings.pushNotifications = it }
                )
            }

            // Default start screen (radio group)
            Text("Default Start Screen", style = MaterialTheme.typography.bodyLarge)
            listOf("Dashboard", "Assignments").forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = settings.defaultStartScreen == option,
                            role = Role.RadioButton,
                            onClick = { settings.defaultStartScreen = option }
                        )
                ) {
                    RadioButton(selected = settings.defaultStartScreen == option, onClick = null)
                    Text(option, modifier = Modifier.padding(start = 12.dp, top = 10.dp, bottom = 10.dp))
                }
            }
        }
    }
}

@Composable
fun AddAssignmentScreen(
    onSave: (Assignment) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf("Medium") }
    var description by remember { mutableStateOf("") }
    var reminder by remember { mutableStateOf(false) }

    val canSave = title.isNotBlank() && course.isNotBlank() && dueDate.isNotBlank()

    Scaffold(
        topBar = {
            AppBar(title = "Add New Assignment", canNavigateBack = true, onBackClick = onCancel)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AssignmentForm(
                    title = title,
                    course = course,
                    dueDate = dueDate,
                    priority = priority,
                    description = description,
                    reminder = reminder,
                    onTitleChange = { title = it },
                    onCourseChange = { course = it },
                    onDueDateChange = { dueDate = it },
                    onPriorityChange = { priority = it },
                    onDescriptionChange = { description = it },
                    onReminderChange = { reminder = it }
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                        Text("Cancel")
                    }
                    Button(
                        onClick = {
                            onSave(
                                Assignment(
                                    id = 0, // real id assigned by the caller
                                    title = title.trim(),
                                    course = course,
                                    dueDate = dueDate.trim(),
                                    priority = priority,
                                    description = description,
                                    reminder = reminder
                                )
                            )
                        },
                        enabled = canSave,
                        modifier = Modifier.weight(1f)
                    ) { Text("Save Assignment") }
                }
            }
        }
    }
}
